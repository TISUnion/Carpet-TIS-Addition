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

package carpettisaddition.logging.loggers.turtleegg;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class TurtleEggLogger extends AbstractLogger
{
	public static final String NAME = "turtleEgg";

	private static final TurtleEggLogger INSTANCE = new TurtleEggLogger();

	private TurtleEggLogger()
	{
		super(NAME, true);
	}

	public static TurtleEggLogger getInstance()
	{
		return INSTANCE;
	}

	public boolean isActivated()
	{
		return TISAdditionLoggerRegistry.__turtleEgg;
	}

	public void onBreakingEgg(Level world, BlockPos pos, BlockState state, Entity entity)
	{
		if (world.isClientSide())
		{
			return;
		}
		this.log(() -> {
			// [O] xxx breaks egg @ {}
			return new BaseComponent[]{Messenger.c(
					entity != null ? Messenger.entity(null, entity) : Messenger.s("?"),
					"r  x ",
					Messenger.block(state.getBlock()),
					"g  @ ",
					Messenger.coord(null, pos, DimensionWrapper.of(world))
			)};
		});
	}
}
