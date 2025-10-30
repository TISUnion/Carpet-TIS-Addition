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

package carpettisaddition.logging.loggers.microtiming.utils;

import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MicroTimingContext
{
	private Level world;
	private BlockPos blockPos;
	private Supplier<BaseEvent> eventSupplier;
	private BiFunction<Level, BlockPos, Optional<DyeColor>> woolGetter;
	private DyeColor color;
	private String blockName;

	public static MicroTimingContext create()
	{
		return new MicroTimingContext();
	}

	public Level getWorld()
	{
		return this.world;
	}

	public BlockPos getBlockPos()
	{
		return this.blockPos;
	}

	public Supplier<BaseEvent> getEventSupplier()
	{
		return this.eventSupplier;
	}

	public BiFunction<Level, BlockPos, Optional<DyeColor>> getWoolGetter()
	{
		return this.woolGetter;
	}

	public DyeColor getColor()
	{
		return this.color;
	}

	public String getBlockName()
	{
		return this.blockName;
	}

	public MicroTimingContext withWorld(Level world)
	{
		this.world = world;
		return this;
	}

	public MicroTimingContext withBlockPos(BlockPos blockPos)
	{
		this.blockPos = blockPos.immutable();
		return this;
	}

	public MicroTimingContext withEventSupplier(Supplier<BaseEvent> eventSupplier)
	{
		this.eventSupplier = eventSupplier;
		return this;
	}

	public MicroTimingContext withEvent(BaseEvent event)
	{
		return this.withEventSupplier(() -> event);
	}

	public MicroTimingContext withWoolGetter(BiFunction<Level, BlockPos, Optional<DyeColor>> woolGetter)
	{
		this.woolGetter = woolGetter;
		return this;
	}

	public MicroTimingContext withColor(DyeColor color)
	{
		this.color = color;
		return this;
	}

	public MicroTimingContext withBlockName(String blockName)
	{
		this.blockName = blockName;
		return this;
	}
}
