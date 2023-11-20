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

package carpettisaddition.commands.speedtest.session;

import carpettisaddition.commands.speedtest.tester.SpeedTester;
import com.google.common.collect.Maps;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class SpeedTestServerSessionHolder
{
	private final Map<UUID, SpeedTestServerSession> sessions = Maps.newConcurrentMap();

	public void abortAndClearFor(ServerPlayerEntity player)
	{
		SpeedTester tester = this.sessions.remove(player.getUuid());
		if (tester != null)
		{
			tester.abort();
		}
	}

	public void clearFor(ServerPlayerEntity player, SpeedTestServerSession expected)
	{
		this.sessions.remove(player.getUuid(), expected);
	}

	public void setFor(ServerPlayerEntity player, SpeedTestServerSession session)
	{
		this.sessions.put(player.getUuid(), session);
	}

	@Nullable
	public SpeedTestServerSession getFor(ServerPlayerEntity player)
	{
		return this.sessions.get(player.getUuid());
	}

	public void reset()
	{
		// useful isEmpty check to prevent class loading error on the SpeedTestCommand#onServerClosed
		// in case the "mod.jar is modified
		if (!this.sessions.isEmpty())
		{
			this.sessions.values().forEach(SpeedTester::abort);
			this.sessions.clear();
		}
	}
}
