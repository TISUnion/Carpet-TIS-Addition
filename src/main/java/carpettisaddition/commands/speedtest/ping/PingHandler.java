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

package carpettisaddition.commands.speedtest.ping;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PingHandler implements PongReceiver
{
	private static final AtomicInteger MAGIC_COUNTER = new AtomicInteger(0);
	private final Random random = new Random();
	private final Map<Long, PingTask> pingTasks = new ConcurrentHashMap<>();
	private final FailureCallback failureCallback;

	public PingHandler(@Nullable FailureCallback failureCallback)
	{
		this.failureCallback = failureCallback;
	}

	public void reset()
	{
		this.pingTasks.clear();
	}

	private void makePingPayload(NbtCompound nbt, long magic)
	{
		nbt.putString("type", "ping");
		nbt.putLong("magic", magic);
		nbt.putLong("timestamp", System.nanoTime());
	}

	private long createMagic()
	{
		return MAGIC_COUNTER.getAndIncrement() | ((long)this.random.nextInt() << 32);
	}

	public void pingClient(ServerPlayNetworkHandler networkHandler, PongCallback pongCallback)
	{
		long magic = this.createMagic();
		this.pingTasks.put(magic, new PingTask(magic, pongCallback));
		TISCMServerPacketHandler.getInstance().sendPacket(
				networkHandler,
				TISCMProtocol.S2C.SPEED_TEST_PING,
				nbt -> this.makePingPayload(nbt, magic)
		);
	}

	public void pingServer(PongCallback pongCallback)
	{
		long magic = this.createMagic();
		this.pingTasks.put(magic, new PingTask(magic, pongCallback));
		TISCMClientPacketHandler.getInstance().sendPacket(
				TISCMProtocol.C2S.SPEED_TEST_PING,
				nbt -> this.makePingPayload(nbt, magic)
		);
	}

	/**
	 * Invoked on network thread
	 */
	@Override
	public void onPongReceived(NbtCompound payload)
	{
		PingTask pingTask = this.readPongImpl(payload);
		if (pingTask != null && pingTask.pongCallback != null)
		{
			pingTask.pongCallback.onPongReceived(payload, pingTask.costNs);
		}
		if (pingTask == null && this.failureCallback != null)
		{
			this.failureCallback.onPongFailed();
		}
	}

	private PingTask readPongImpl(NbtCompound payload)
	{
		String pingType = payload.getString("type");
		if (!pingType.equals("pong"))
		{
			CarpetTISAdditionMod.LOGGER.warn("bad ping type, found {}, should be pong", pingType);
			return null;
		}

		long magic = payload.getLong("magic");
		PingTask pingTask = this.pingTasks.remove(magic);
		if (pingTask == null)
		{
			CarpetTISAdditionMod.LOGGER.warn("pong magic mismatch, received {}, should be in {}", magic, new ArrayList<>(this.pingTasks.keySet()));
			return null;
		}

		pingTask.costNs = System.nanoTime() - payload.getLong("timestamp");
		return pingTask;
	}

	private static class PingTask
	{
		public final long magic;
		public final PongCallback pongCallback;
		public long costNs = -1;

		public PingTask(long magic, PongCallback pongCallback)
		{
			this.magic = magic;
			this.pongCallback = pongCallback;
		}
	}

	@FunctionalInterface
	public interface PongCallback
	{
		void onPongReceived(NbtCompound payload, long pingNs);
	}

	@FunctionalInterface
	public interface FailureCallback
	{
		void onPongFailed();
	}
}
