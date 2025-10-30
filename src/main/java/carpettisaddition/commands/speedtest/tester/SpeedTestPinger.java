/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.speedtest.tester;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.commands.speedtest.SpeedTestCommand;
import carpettisaddition.commands.speedtest.TestType;
import carpettisaddition.commands.speedtest.ping.PingHandler;
import carpettisaddition.commands.speedtest.ping.PongReceiver;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSession;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSessionHolder;
import carpettisaddition.commands.speedtest.session.SpeedTestSessionMessenger;
import carpettisaddition.commands.speedtest.session.SpeedTestSessionMessengerImpl;
import carpettisaddition.translations.TranslationContext;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SpeedTestPinger extends TranslationContext implements SpeedTestServerSession, PongReceiver
{
	private final ServerPlayer player;
	private final int count;
	private final double intervalSec;
	private final SpeedTestServerSessionHolder sessionHolder;
	private final SpeedTestSessionMessenger messenger;
	private final Semaphore pongSemaphore = new Semaphore(0);
	private final Semaphore abortSemaphore = new Semaphore(0);
	private final PingHandler pingHandler = new PingHandler(this::abortCuzBadPongReceived);

	private final List<Long> pingNsList = Lists.newArrayList();

	public SpeedTestPinger(CommandSourceStack source, ServerPlayer player, int count, double intervalSec, SpeedTestServerSessionHolder sessionHolder)
	{
		super(SpeedTestCommand.getInstance().getTranslator().getDerivedTranslator("ping_test"));
		this.player = player;
		this.count = count;
		this.intervalSec = intervalSec;
		this.sessionHolder = sessionHolder;
		this.messenger = new SpeedTestSessionMessengerImpl(TestType.PING, source);
	}

	@Override
	public void start()
	{
		this.sessionHolder.setFor(this.player, this);
		this.messenger.sendMessage(tr(
				"start",
				this.count,
				Math.round(this.intervalSec * 1e3)
		), true);

		Thread thread = new Thread(this::pingThread);
		thread.setName("TISCM-" + this.getClass().getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	private void pingThread()
	{
		try
		{
			this.pingLoop();
		}
		catch (InterruptedException e)
		{
			CarpetTISAdditionMod.LOGGER.error("{} is interrupted by {}", this.getClass().getSimpleName(), e);
		}
	}

	private void pingLoop() throws InterruptedException
	{
		for (int i = 1; i <= this.count; i++)
		{
			AtomicLong pingCost = new AtomicLong(0);
			int index = i;

			this.pingHandler.pingClient(
					this.player.connection,
					(payload, pingNs) -> {
						synchronized (this.pingNsList)
						{
							this.pingNsList.add(pingNs);
						}

						this.messenger.sendMessage(tr(
								"pinging",
								index,
								String.format("%.1f", pingNs / 1e6)
						), true);

						pingCost.set(pingNs);
						this.pongSemaphore.release();
					}
			);

			this.pongSemaphore.acquire();  // wait for the pong
			long nextPingWaitNs = i < this.count ? Math.max(0, (long)(this.intervalSec * 1e9 - pingCost.get())) : 0;
			if (this.abortSemaphore.tryAcquire(nextPingWaitNs, TimeUnit.NANOSECONDS))
			{
				break;
			}
		}

		this.sessionHolder.clearFor(this.player, this);
		this.report();
	}

	private void abortCuzBadPongReceived()
	{
		CarpetTISAdditionMod.LOGGER.warn("bad pong received, abort test");
		this.abort();
	}

	private void report()
	{
		int cnt;
		double pingMs;
		synchronized (this.pingNsList)
		{
			cnt = this.pingNsList.size();
			double pingNs = this.pingNsList.stream().
					mapToDouble(Long::doubleValue).
					average().
					orElse(-1);
			pingMs = pingNs / 1e6;
		}

		this.messenger.sendMessage(tr(
				"done",
				cnt,
				String.format("%.2f", pingMs)
		), false);
	}

	@Override
	public void abort()
	{
		// it might be removed from the server session holder,
		// which means the incoming pong packet can never reach here
		this.pongSemaphore.release();

		this.abortSemaphore.release();
	}

	@Override
	public SpeedTestSessionMessenger getMessenger()
	{
		return this.messenger;
	}

	@Override
	public void onPongReceived(CompoundTag payload)
	{
		this.pingHandler.onPongReceived(payload);
	}
}
