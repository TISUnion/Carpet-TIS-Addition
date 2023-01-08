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

import carpet.script.annotation.Locator;
import carpet.script.annotation.ScarpetFunction;
import carpet.script.value.ListValue;
import carpet.script.value.Value;
import carpet.script.value.ValueConversions;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import java.util.stream.Collectors;

public class Functions
{
	@ScarpetFunction(maxParams = 3)
	public boolean register_block(@Locator.Block BlockPos pos)
	{
		return MicroTimingLoggerManager.trackedPositions.add(pos);
	}

	@ScarpetFunction(maxParams = 3)
	public boolean unregister_block(@Locator.Block BlockPos pos)
	{
		return MicroTimingLoggerManager.trackedPositions.remove(pos);
	}

	@ScarpetFunction
	public ListValue registered_blocks()
	{
		List<Value> blockList = MicroTimingLoggerManager.trackedPositions.stream().map(ValueConversions::of).collect(Collectors.toList());
		return new ListValue(blockList);
	}

	@ScarpetFunction(maxParams = 3)
	public boolean is_registered(@Locator.Block BlockPos pos)
	{
		return MicroTimingLoggerManager.trackedPositions.contains(pos);
	}
}