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
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.world.World;

public class BlockEventSubStage extends AbstractSubStage
{
	private final World world;
	private final BlockAction blockEventData;
	private final int order;
	private final int depth;

	public BlockEventSubStage(World world, BlockAction blockEventData, int order, int depth)
	{
		this.world = world;
		this.blockEventData = blockEventData;
		this.order = order;
		this.depth = depth;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				MicroTimingLoggerManager.tr("common.block"), "w : ", Messenger.block(this.blockEventData.getBlock()), Messenger.newLine(),
				MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order),
				MicroTimingLoggerManager.tr("common.depth"), String.format("w : %d\n", this.depth),
				MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtil.coord(this.blockEventData.getPos()))
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.blockEventData.getPos(), DimensionWrapper.of(this.world)));
	}
}
