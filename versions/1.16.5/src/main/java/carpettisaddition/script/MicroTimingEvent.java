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

package carpettisaddition.script;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.ListValue;
import carpet.script.value.StringValue;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.microtiming.events.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.Arrays;

public class MicroTimingEvent extends Event
{
	public static void noop() 
	{
		//to load events before scripts do
	}

	public MicroTimingEvent(String name, int reqArgs, boolean isGlobalOnly)
	{
		super(name, reqArgs, isGlobalOnly);
	}

	public static void determineBlockEvent(BaseEvent event, Level world, BlockPos pos)
	{
		if (event instanceof DetectBlockUpdateEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("detected_block_update", pos, world.dimensionType());
		}
		if (event instanceof BlockStateChangeEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("block_state_changed", pos, world.dimensionType());
		}
		if (event instanceof ExecuteBlockEventEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("executed_block_event", pos, world.dimensionType());
		}
		if (event instanceof ExecuteTileTickEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("executed_tile_tick", pos, world.dimensionType());
		}
		if (event instanceof EmitBlockUpdateEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("emitted_block_update", pos, world.dimensionType());
		}
		if (event instanceof EmitBlockUpdateRedstoneDustEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("emitted_block_update_redstone_dust", pos, world.dimensionType());
		}
		if (event instanceof ScheduleBlockEventEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("scheduled_block_event", pos, world.dimensionType());
		}
		if (event instanceof ScheduleTileTickEvent)
		{
			MICRO_TIMING_EVENT.onMicroTimingEvent("scheduled_tile_tick", pos, world.dimensionType());
		}
	}

	public void onMicroTimingEvent(String type, BlockPos pos, DimensionType dimension)
	{
	}

	public static MicroTimingEvent MICRO_TIMING_EVENT = new MicroTimingEvent("microtiming_event", 2, true)
	{
		@Override
		public void onMicroTimingEvent(String type, BlockPos pos, DimensionType dimension)
		{
			this.handler.call(
					() -> Arrays.asList(
							StringValue.of(type),
							ListValue.fromTriple(pos.getX(), pos.getY(), pos.getZ()),
							StringValue.of(dimension.toString())
					),
					() -> CarpetTISAdditionServer.minecraft_server.createCommandSourceStack()
			);
		}
	};
}