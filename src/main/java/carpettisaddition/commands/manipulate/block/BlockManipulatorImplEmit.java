/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.manipulate.block;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

class BlockManipulatorImplEmit extends TranslationContext
{
	public BlockManipulatorImplEmit(Translator translator)
	{
		super(translator);
	}

	public void emitBlockUpdateAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();

		// ref: net.minecraft.world.World#setBlockState
		world.updateNeighbors(blockPos, block);
		if (blockState.hasComparatorOutput())
		{
			world.updateHorizontalAdjacent(blockPos, block);
		}

		Messenger.tell(source, tr("emit.block_update", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
	}

	public void emitStateUpdateAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);

		// ref: net.minecraft.world.World#setBlockState
		int flags = 2;
		//#if MC >= 11600
		//$$ int maxUpdateDepth = 512;
		//$$ blockState.updateNeighbors(world, blockPos, flags, maxUpdateDepth);
		//$$ blockState.prepare(world, blockPos, flags , maxUpdateDepth);
		//#else
		blockState.updateNeighborStates(world, blockPos, flags);
		blockState.method_11637(world, blockPos, flags);
		//#endif

		Messenger.tell(source, tr("emit.state_update", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
	}

	public void emitLightUpdateAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);

		world.getChunkManager().getLightingProvider().checkBlock(blockPos);

		Messenger.tell(source, tr("emit.light_update", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
	}
}
