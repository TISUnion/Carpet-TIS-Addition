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

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.network.packet.s2c.play.BlockActionS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

//#if MC >= 11700
//$$ import net.minecraft.block.Blocks;
//$$ import net.minecraft.world.biome.Biome;
//#endif

class BlockManipulatorImplExecute extends TranslationContext
{
	public BlockManipulatorImplExecute(Translator translator)
	{
		super(translator);
	}

	public void executeBlockEvent(ServerCommandSource source, BlockPos blockPos, int type, int data)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);
		//#if MC >= 11600
		//$$ blockState.onSyncedBlockEvent
		//#else
		blockState.onBlockAction
				//#endif
						(world, blockPos, type, data);
		source.getMinecraftServer().getPlayerManager().sendToAround(
				null,
				blockPos.getX(),
				blockPos.getY(),
				blockPos.getZ(),
				CarpetTISAdditionSettings.blockEventPacketRange,
				//#if MC >= 11600
				//$$ world.getRegistryKey(),
				//#else
				world.getDimension().getType(),
				//#endif
				new BlockActionS2CPacket(blockPos, blockState.getBlock(), type, data)
		);
	}

	public void executeTileTickAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);
		FluidState fluidState = world.getFluidState(blockPos);

		blockState.scheduledTick(world, blockPos, world.getRandom());
		fluidState.onScheduledTick(
				world, blockPos
				//#if MC >= 12103
				//$$ , blockState
				//#endif
		);
	}

	public void executeRandomTickAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);
		FluidState fluidState = world.getFluidState(blockPos);

		// ref: net.minecraft.server.world.ServerWorld#tickChunk
		//#if MC >= 11500
		blockState.randomTick
				//#else
				//$$ blockState.onRandomTick
				//#endif
						(world, blockPos, world.getRandom());

		fluidState.onRandomTick(world, blockPos, world.getRandom());
	}

	public void executePrecipitationTickAt(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);

		// ref: net.minecraft.server.world.ServerWorld#tickChunk
		//#if MC >= 11700
		//$$ //#if MC >= 11800
		//$$ //$$ Biome biome = world.getBiome(blockPos.up()).value();
		//$$ //#else
		//$$ Biome biome = world.getBiome(blockPos.up());
		//$$ //#endif
		//$$
		//$$ Biome.Precipitation precipitation = biome.getPrecipitation(
		//$$ 		//#if MC >= 11900
		//$$ 		//$$ blockPos
		//$$ 		//#endif
		//$$ 		//#if MC >= 12103
		//$$ 		//$$ , world.getSeaLevel()
		//$$ 		//#endif
		//$$ );
		//$$
		//$$ //#if MC >= 11900
		//$$ //$$ if (precipitation != Biome.Precipitation.NONE)
		//$$ //$$ {
		//$$ //$$ 	blockState.getBlock().precipitationTick(blockState, world, blockPos, precipitation);
		//$$ //$$ }
		//$$ //#else
		//$$ if (precipitation == Biome.Precipitation.RAIN && biome.isCold(blockPos))
		//$$ {
		//$$ 	precipitation = Biome.Precipitation.SNOW;
		//$$ }
		//$$ blockState.getBlock().precipitationTick(blockState, world, blockPos, precipitation);
		//$$ //#endif
		//#else
		blockState.getBlock().rainTick(world, blockPos);
		//#endif
	}
}
