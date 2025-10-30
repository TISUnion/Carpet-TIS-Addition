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

import carpettisaddition.commands.speedtest.SpeedTestReporter;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSession;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSessionHolder;
import carpettisaddition.commands.speedtest.session.SpeedTestSessionMessenger;
import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.server.level.ServerPlayer;

public class SpeedTestServerUploadReceiver extends SpeedTesterBase implements SpeedTestServerSession
{
	private final ServerPlayer player;
	private final SpeedTestServerSessionHolder serverSessionHolder;
	private final SpeedTestReporter reporter;

	public SpeedTestServerUploadReceiver(ServerPlayer player, int totalSizeMb, SpeedTestServerSessionHolder serverSessionHolder, SpeedTestReporter reporter)
	{
		super(totalSizeMb);
		this.player = player;
		this.serverSessionHolder = serverSessionHolder;
		this.reporter = reporter;
	}

	@Override
	public SpeedTestSessionMessenger getMessenger()
	{
		return this.reporter;
	}

	@Override
	public void start()
	{
		this.reporter.start(this.totalSize);
		super.start();
		TISCMServerPacketHandler.getInstance().sendPacket(
				this.player.connection,
				TISCMProtocol.S2C.SPEED_TEST_UPLOAD_REQUEST,
				nbt -> nbt.putInt("size_mb", this.totalSizeMb)
		);
		this.serverSessionHolder.setFor(this.player, this);
	}

	@Override
	public void abort()
	{
		super.abort();
		TISCMServerPacketHandler.getInstance().sendPacket(
				this.player.connection,
				TISCMProtocol.S2C.SPEED_TEST_ABORT,
				nbt -> {}
		);
	}

	public void onC2SPayloadPacketReceived()
	{
		this.onPayloadDone();
	}

	@Override
	protected void onProgress(long sentBytes, long totalBytes)
	{
		this.reporter.onProgress(this.timer.getTimeElapsedNs(), sentBytes, totalBytes);
	}

	@Override
	protected void onDone(long timeCostNs)
	{
		this.reporter.reportDone(timeCostNs, this.processedSize.get(), this.totalSize);
		this.serverSessionHolder.clearFor(this.player, this);
	}
}
