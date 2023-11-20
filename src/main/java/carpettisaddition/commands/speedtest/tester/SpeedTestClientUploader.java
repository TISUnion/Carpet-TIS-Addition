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

import carpettisaddition.commands.speedtest.SpeedTestPacketUtils;
import carpettisaddition.commands.speedtest.ping.PingHandler;
import carpettisaddition.commands.speedtest.session.SpeedTestClientSessionHolder;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;

public class SpeedTestClientUploader extends SpeedTesterPayloadSenderBase
{
	private final SpeedTestClientSessionHolder clientSessionHolder;

	public SpeedTestClientUploader(int totalSizeMb, SpeedTestClientSessionHolder clientSessionHolder)
	{
		super(totalSizeMb);
		this.clientSessionHolder = clientSessionHolder;
	}

	@Override
	public void start()
	{
		super.start();
		this.clientSessionHolder.setUploader(this);
	}

	@Override
	protected void sendOnePacket()
	{
		TISCMClientPacketHandler.getInstance().sendPacket(
				TISCMProtocol.C2S.SPEED_TEST_UPLOAD_PAYLOAD,
				nbt -> nbt.putByteArray("buf", SpeedTestPacketUtils.getPayloadByteArray()),
				this::onPayloadDone
		);
	}

	@Override
	protected void sendPingPacket(PingHandler pingHandler, PingHandler.PongCallback pongCallback)
	{
		pingHandler.pingServer(pongCallback);
	}

	@Override
	protected void onDone(long timeCostNs)
	{
		this.clientSessionHolder.clear();
	}
}
