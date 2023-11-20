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
import carpettisaddition.commands.speedtest.SpeedTestPacketUtils;
import carpettisaddition.commands.speedtest.ping.PingHandler;
import carpettisaddition.commands.speedtest.ping.PongReceiver;
import net.minecraft.nbt.CompoundTag;

public abstract class SpeedTesterPayloadSenderBase extends SpeedTesterBase implements PongReceiver
{
	protected final int packetCount;
	private final PingHandler pingHandler = new PingHandler(this::abortCuzBadPongReceived);

	public SpeedTesterPayloadSenderBase(int testSizeMb)
	{
		super(testSizeMb);
		this.packetCount = SpeedTestPacketUtils.getPacketCount(testSizeMb);
	}

	@Override
	public void start()
	{
		super.start();
		for (int i = 0; i < Math.min(3, this.packetCount); i++)
		{
			this.sendOnePacket();
		}
	}

	@Override
	protected void onContinue(int processedCount)
	{
		// ping every 16 packets (256KiB) or before the final packet
		// to ensure the payloads are actually delivered, not being buffered somewhere (e.g. in proxy server).
		// maximum buffered size: 16KiB/packet * 16packet * 3concurrency == 768KiB, acceptable
		if (processedCount % 16 == 0 || processedCount == this.packetCount - 1)
		{
			this.sendPingPacket(this.pingHandler, (p, c) -> this.sendOnePacket());
		}
		else
		{
			this.sendOnePacket();
		}
	}

	@Override
	public void onPongReceived(CompoundTag payload)
	{
		this.pingHandler.onPongReceived(payload);
	}

	protected void abortCuzBadPongReceived()
	{
		CarpetTISAdditionMod.LOGGER.warn("bad pong received, abort test");
		this.abort();
	}

	/**
	 * Use {@link SpeedTesterBase#onPayloadDone} as the callback arg in the packet sending method
	 */
	protected abstract void sendOnePacket();

	protected abstract void sendPingPacket(PingHandler pingHandler, PingHandler.PongCallback pongCallback);
}

