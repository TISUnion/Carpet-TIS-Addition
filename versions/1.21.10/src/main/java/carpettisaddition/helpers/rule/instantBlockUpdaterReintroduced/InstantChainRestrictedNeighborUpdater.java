/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced;

import carpettisaddition.logging.loggers.microtiming.utils.InstantNeighborUpdater;
import carpettisaddition.mixins.rule.instantBlockUpdaterReintroduced.ChainRestrictedNeighborUpdaterAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraft.world.level.redstone.InstantNeighborUpdater;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

/**
 * A class that appears as {@link CollectingNeighborUpdater} but is actually implemented as {@link InstantNeighborUpdater}
 * <p>
 * It should override all public methods of {@link CollectingNeighborUpdater} whose behavior differs from that of {@link InstantNeighborUpdater}
 */
public class InstantChainRestrictedNeighborUpdater extends CollectingNeighborUpdater implements InstantNeighborUpdater
{
	private final InstantNeighborUpdater simpleNeighborUpdater;

	public InstantChainRestrictedNeighborUpdater(Level world, int maxChainDepth)
	{
		super(world, maxChainDepth);
		this.simpleNeighborUpdater = new InstantNeighborUpdater(world);
	}

	private void onBlockUpdate(BlockPos blockPos)
	{
		var callback = ((ChainRestrictedNeighborUpdaterAccessor)this).getNeighborUpdateCallback$TISCM();
		if (callback != null)
		{
			callback.accept(blockPos);
		}
	}

	@Override
	public void replaceWithStateForNeighborUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.replaceWithStateForNeighborUpdate(direction, neighborState, pos, neighborPos, flags, maxUpdateDepth);
	}

	@Override
	public void updateNeighbor(BlockPos pos, Block sourceBlock, @Nullable Orientation orientation)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.updateNeighbor(pos, sourceBlock, orientation);
	}

	@Override
	public void updateNeighbor(BlockState state, BlockPos pos, Block sourceBlock, @Nullable Orientation orientation, boolean notify)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.updateNeighbor(state, pos, sourceBlock, orientation, notify);
	}

	@Override
	public void updateNeighbors(BlockPos pos, Block sourceBlock, @Nullable Direction except, @Nullable Orientation orientation)
	{
		for (Direction direction : UPDATE_ORDER)
		{
			if (direction != except)
			{
				this.onBlockUpdate(pos.offset(direction));
			}
		}
		this.simpleNeighborUpdater.updateNeighbors(pos, sourceBlock, except, orientation);
	}
}
