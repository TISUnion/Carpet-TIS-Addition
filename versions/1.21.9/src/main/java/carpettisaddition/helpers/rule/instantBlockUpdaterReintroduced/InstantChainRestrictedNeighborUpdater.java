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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import net.minecraft.world.block.SimpleNeighborUpdater;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

/**
 * A class that appears as {@link ChainRestrictedNeighborUpdater} but is actually implemented as {@link SimpleNeighborUpdater}
 * <p>
 * It should override all public methods of {@link ChainRestrictedNeighborUpdater} whose behavior differs from that of {@link SimpleNeighborUpdater}
 */
public class InstantChainRestrictedNeighborUpdater extends ChainRestrictedNeighborUpdater implements InstantNeighborUpdater
{
	private final SimpleNeighborUpdater simpleNeighborUpdater;

	public InstantChainRestrictedNeighborUpdater(World world, int maxChainDepth)
	{
		super(world, maxChainDepth);
		this.simpleNeighborUpdater = new SimpleNeighborUpdater(world);
	}

	private void onBlockUpdate(BlockPos blockPos)
	{
		var listener = ((ChainRestrictedNeighborUpdaterAccessor)this).getBlockUpdateListener$TISCM();
		if (listener != null)
		{
			listener.accept(blockPos);
		}
	}

	@Override
	public void replaceWithStateForNeighborUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.replaceWithStateForNeighborUpdate(direction, neighborState, pos, neighborPos, flags, maxUpdateDepth);
	}

	@Override
	public void updateNeighbor(BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.updateNeighbor(pos, sourceBlock, orientation);
	}

	@Override
	public void updateNeighbor(BlockState state, BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, boolean notify)
	{
		this.onBlockUpdate(pos);
		this.simpleNeighborUpdater.updateNeighbor(state, pos, sourceBlock, orientation, notify);
	}

	@Override
	public void updateNeighbors(BlockPos pos, Block sourceBlock, @Nullable Direction except, @Nullable WireOrientation orientation)
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
