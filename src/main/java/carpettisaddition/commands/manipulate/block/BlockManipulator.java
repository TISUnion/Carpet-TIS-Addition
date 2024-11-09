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
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.BlockActionS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.arguments.BlockPosArgumentType.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//#if MC >= 11700
//$$ import net.minecraft.block.Blocks;
//$$ import net.minecraft.world.biome.Biome;
//#endif

public class BlockManipulator extends AbstractManipulator
{
	public BlockManipulator()
	{
		super("block");
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		context.node.
				then(argument("target", blockPos()).
						then(literal("execute").
								then(literal("blockevent").
										then(argument("type", integer()).
												then(argument("data", integer()).
														executes(c -> triggerBlockEvent(
																c.getSource(),
																getBlockPos(c, "target"),
																getInteger(c, "type"),
																getInteger(c, "data")
														))
												)
										)
								).
								then(
										literal("tiletick").
												executes(c -> triggerTileTick(c.getSource(), getBlockPos(c, "target")))
								).
								then(
										literal("randomtick").
												executes(c -> triggerRandomTick(c.getSource(), getBlockPos(c, "target")))
								).
								then(
										literal("precipitationtick").
												executes(c -> triggerPrecipitationTick(c.getSource(), getBlockPos(c, "target")))
								)
						)
				);
	}

	private int triggerBlockEvent(ServerCommandSource source, BlockPos blockPos, int type, int data)
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

		Messenger.tell(source, tr("execute.block_event", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world)), type, data));
		return 1;
	}

	private int triggerTileTick(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);
		blockState.scheduledTick(world, blockPos, world.getRandom());
		Messenger.tell(source, tr("execute.tile_tick", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
		return 1;
	}

	private int triggerRandomTick(ServerCommandSource source, BlockPos blockPos)
	{
		ServerWorld world = source.getWorld();
		BlockState blockState = world.getBlockState(blockPos);

		// ref: net.minecraft.server.world.ServerWorld#tickChunk
		//#if MC >= 11500
		blockState.randomTick
		//#else
		//$$ blockState.onRandomTick
		//#endif
				(world, blockPos, world.getRandom());

		Messenger.tell(source, tr("execute.random_tick", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
		return 1;
	}

	private int triggerPrecipitationTick(ServerCommandSource source, BlockPos blockPos)
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

		Messenger.tell(source, tr("execute.precipitation_tick", Messenger.block(blockState), Messenger.coord(blockPos, DimensionWrapper.of(world))));
		return 1;
	}
}
