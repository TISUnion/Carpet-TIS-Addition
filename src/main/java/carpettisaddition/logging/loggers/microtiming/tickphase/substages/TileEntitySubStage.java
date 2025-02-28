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
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySubStage extends AbstractSubStage
{
	private final World world;
	private final BlockPos pos;
	private final Block block;
	private final int order;

	public TileEntitySubStage(BlockEntity tileEntity, int order)
	{
		this.world = tileEntity.getWorld();
		this.pos = tileEntity.getPos().toImmutable();
		this.block = tileEntity.getCachedState().getBlock();
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				MicroTimingLoggerManager.tr("common.block"), "w : ", Messenger.block(this.block), Messenger.newLine(),
				MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order),
				MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtils.coord(this.pos))
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return Messenger.ClickEvents.suggestCommand(TextUtils.tp(this.pos, DimensionWrapper.of(this.world)));
	}
}
