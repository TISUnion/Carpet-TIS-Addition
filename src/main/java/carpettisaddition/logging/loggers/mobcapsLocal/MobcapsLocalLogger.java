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

package carpettisaddition.logging.loggers.mobcapsLocal;

import carpet.logging.HUDLogger;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.BaseComponent;

// a placeholder class, its implementation is in 1.18+
public class MobcapsLocalLogger extends AbstractHUDLogger
{
	public static final String NAME = "mobcapsLocal";
	private static final MobcapsLocalLogger INSTANCE = new MobcapsLocalLogger();

	private MobcapsLocalLogger()
	{
		super(NAME, true);
	}

	public static MobcapsLocalLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		return null;
	}

	@Override
	public HUDLogger createCarpetLogger()
	{
		throw new RuntimeException("MobcapsLocal logger can only be used in mc1.18+");
	}

	public void onServerClosed()
	{
	}
}