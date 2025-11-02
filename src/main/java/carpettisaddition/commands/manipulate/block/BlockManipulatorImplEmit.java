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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

class BlockManipulatorImplEmit extends TranslationContext
{
	public BlockManipulatorImplEmit(Translator translator)
	{
		super(translator);
	}

	public void emitBlockUpdateAt(CommandSourceStack source, BlockPos blockPos)
	{
		ServerLevel world = source.getLevel();
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();

		// ref: net.minecraft.world.World#setBlockState
		world.blockUpdated(blockPos, block);
		if (blockState.hasAnalogOutputSignal())
		{
			world.updateNeighbourForOutputSignal(blockPos, block);
		}
	}

	public void emitStateUpdateAt(CommandSourceStack source, BlockPos blockPos)
	{
		ServerLevel world = source.getLevel();
		BlockState blockState = world.getBlockState(blockPos);

		// ref: net.minecraft.world.World#setBlockState
		int flags = 2;
		//#if MC >= 11600
		//$$ int maxUpdateDepth = 512;
		//$$ blockState.updateNeighbourShapes(world, blockPos, flags, maxUpdateDepth);
		//$$ blockState.updateIndirectNeighbourShapes(world, blockPos, flags , maxUpdateDepth);
		//#else
		blockState.updateNeighbourShapes(world, blockPos, flags);
		blockState.updateIndirectNeighbourShapes(world, blockPos, flags);
		//#endif
	}

	public void emitLightUpdateAt(CommandSourceStack source, BlockPos blockPos)
	{
		ThreadedLevelLightEngine lightingProvider = source.getLevel().getChunkSource().getLightEngine();
		lightingProvider.checkBlock(blockPos);
	}
}
