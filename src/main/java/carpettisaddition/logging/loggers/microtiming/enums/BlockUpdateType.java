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

package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.logger.microtiming.misc.BlockAccessor;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.List;

//#if MC >= 11900
//$$ import net.minecraft.world.block.NeighborUpdater;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.block.AbstractBlock;
//#else
import net.minecraft.world.level.block.Block;
//#endif

public enum BlockUpdateType
{
	BLOCK_UPDATE("BlockUpdates", new String[]{"Neighbor Changed", "Neighbor Update"}, Constants.BLOCK_UPDATE_ORDER),
	BLOCK_UPDATE_EXCEPT("BlockUpdates Except", new String[]{"Neighbor Changed Except", "Neighbor Update Except"}, Constants.BLOCK_UPDATE_ORDER),
	STATE_UPDATE("StateUpdates", new String[]{"Post Placement", "Update Shape"}, Constants.STATE_UPDATE_ORDER),
	COMPARATOR_UPDATE("ComparatorUpdates", new String[]{"Block update only to comparators"}, Constants.COMPARATOR_UPDATE_ORDER),
	SINGLE_BLOCK_UPDATE("SingleBlockUpdate", new String[]{"Block update to a single block"}, new Direction[]{}),
	SINGLE_STATE_UPDATE("SingleStateUpdate", new String[]{"State update to a single block"}, new Direction[]{});

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("block_update_type");
	private final String name;
	private final String[] aka;
	private final Direction[] updateOrder;

	BlockUpdateType(String name, String[] aka, Direction[] updateOrder)
	{
		this.name = name;
		this.aka = aka;
		this.updateOrder = updateOrder;
	}

	public BaseComponent toText()
	{
		return translator.tr(this.name.toLowerCase().replace(' ', '_'));
	}

	public BaseComponent getUpdateOrderList(Direction skipSide)
	{
		int counter = 0;
		List<Object> builder = Lists.newArrayList();
		builder.add(translator.tr("aka", Joiner.on(", ").join(this.aka)));
		if (this.updateOrder.length > 0)
		{
			builder.add(Messenger.newLine());
			for (Direction direction : this.updateOrder)
			{
				if (skipSide != direction)
				{
					if (counter > 0)
					{
						builder.add(Messenger.newLine());
					}
					builder.add(Messenger.s(String.format("%d. ", ++counter)));
					builder.add(MicroTimingUtil.getFormattedDirectionText(direction));
				}
			}
		}
		if (skipSide != null)
		{
			builder.add(Messenger.s(String.format("\n%s: %s", translator.tr("except"), skipSide)));
		}
		return Messenger.c(builder.toArray(new Object[0]));
	}

	//#if MC < 11600
	@SuppressWarnings("deprecation")
	//#endif
	private static class Constants
	{
		/**
		 * See {@link Level#updateNeighborsAlways} and {@link Level#updateNeighborsExcept}
		 */
		private static final Direction[] BLOCK_UPDATE_ORDER =
				//#if MC >= 11900
				//$$ NeighborUpdater.UPDATE_ORDER;
				//#else
				new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
				//#endif

		/**
		 * (<=1.15) See {@link Block#updateNeighborStates}
		 * (>=1.16) See {@link AbstractBlock.AbstractBlockState#updateNeighbors}
		 */
		private static final Direction[] STATE_UPDATE_ORDER = BlockAccessor.getFACINGS();

		/**
		 * (<=1.15) See {@link Level#updateHorizontalAdjacent}
		 * (>=1.16) See {@link Level#updateComparators}
		 */
		private static final Direction[] COMPARATOR_UPDATE_ORDER = Lists.newArrayList(Direction.Plane.HORIZONTAL.iterator()).toArray(new Direction[0]);
	}
}
