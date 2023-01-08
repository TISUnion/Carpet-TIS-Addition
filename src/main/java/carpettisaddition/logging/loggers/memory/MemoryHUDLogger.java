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

package carpettisaddition.logging.loggers.memory;

import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;

public class MemoryHUDLogger extends AbstractHUDLogger
{
	public static final String NAME = "memory";

	private static final MemoryHUDLogger INSTANCE = new MemoryHUDLogger();

	private MemoryHUDLogger()
	{
		super(NAME, true);
	}

	public static MemoryHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		final long bytesPerMB = 1024 * 1024;
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		long max = Runtime.getRuntime().maxMemory();

		long usedMB = Math.max(total - free, 0) / bytesPerMB;
		long allocatedMB = total / bytesPerMB;
		long maxMB = max != Long.MAX_VALUE ? max / bytesPerMB : -1;
		return new BaseText[]{
				Messenger.c(String.format("g %dM / %dM | %dM", usedMB, allocatedMB, maxMB))
		};
	}
}
