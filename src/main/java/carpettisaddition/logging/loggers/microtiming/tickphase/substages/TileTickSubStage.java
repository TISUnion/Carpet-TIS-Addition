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

package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;

public class TileTickSubStage extends AbstractSubStage
{
	private final World world;
	private final ScheduledTick<?> nextTickListEntry;
	private final int order;

	public TileTickSubStage(World world, ScheduledTick<?> nextTickListEntry, int order)
	{
		this.world = world;
		this.nextTickListEntry = nextTickListEntry;
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		BlockPos pos =
				//#if MC >= 11800
				//$$ this.nextTickListEntry.pos();
				//#else
				this.nextTickListEntry.pos;
				//#endif

		TickPriority priority =
				//#if MC >= 11800
				//$$ this.nextTickListEntry.priority();
				//#else
				this.nextTickListEntry.priority;
				//#endif

		Object target = this.nextTickListEntry.getObject();
		List<Object> list = Lists.newArrayList();

		if (target instanceof Block)
		{
			list.add(Messenger.c(MicroTimingLoggerManager.tr("common.block"), "w : ", Messenger.block((Block)target)));
		}
		else if (target instanceof Fluid)
		{
			list.add(Messenger.c(MicroTimingLoggerManager.tr("common.fluid"), "w : ", Messenger.fluid((Fluid)target)));
		}
		list.add(Messenger.newLine());

		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order)));
		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.priority"), String.format("w : %d (%s)\n", priority.getIndex(), priority)));
		list.add(Messenger.c(MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtil.coord(pos))));

		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public ClickEvent getClickEvent()
	{
		BlockPos pos =
				//#if MC >= 11800
				//$$ this.nextTickListEntry.pos();
				//#else
				this.nextTickListEntry.pos;
				//#endif

		return Messenger.ClickEvents.suggestCommand(TextUtil.tp(pos, DimensionWrapper.of(this.world)));
	}
}
