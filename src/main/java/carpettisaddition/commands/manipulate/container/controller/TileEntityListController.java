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

package carpettisaddition.commands.manipulate.container.controller;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.mixins.command.manipulate.container.WorldAccessor;
import carpettisaddition.utils.IdentifierUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//#if MC >= 11700
import carpettisaddition.utils.GameUtil;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
//#endif

public class TileEntityListController extends AbstractEntityListController
{
	private static final int TOP_N = 10;

	public TileEntityListController()
	{
		super("tile_entity");
	}

	@Override
	protected boolean canManipulate(ServerWorld world)
	{
		return !((WorldAccessor)world).isIteratingTickingBlockEntities();
	}

	@Override
	protected int processWholeList(ServerWorld world, Consumer<List<?>> collectionOperator)
	{
		//#if MC >= 11700
		List<BlockEntityTickInvoker> blockEntityTickers = ((WorldAccessor)world).getBlockEntityTickers();
		collectionOperator.accept(blockEntityTickers);
		return blockEntityTickers.size();
		//#else
		//$$ collectionOperator.accept(world.tickingBlockEntities);
		//$$ return world.tickingBlockEntities.size();
		//#endif
	}

	//#if MC >= 11700
	private static List<BlockEntity> extractFromTicker(Collection<BlockEntityTickInvoker> tickers)
	{
		return tickers.stream().
				map(GameUtil::getBlockEntityFromTickInvoker).
				filter(Objects::nonNull).
				collect(Collectors.toList());
	}

	private static List<BlockEntity> getTickingBlockEntities(ServerWorld world)
	{
		return extractFromTicker(((WorldAccessor)world).getBlockEntityTickers());
	}
	//#endif

	private int queryTileEntityInfo(ServerCommandSource source, BlockPos pos)
	{
		ServerWorld world = source.getWorld();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
		{
			List<BlockEntity> blockEntityList =
					//#if MC >= 11700
					getTickingBlockEntities(world);
					//#else
					//$$ world.tickingBlockEntities;
					//#endif

			int index = blockEntityList.indexOf(blockEntity);
			Messenger.tell(source, Arrays.asList(
					tr("query.title", Messenger.coord(pos, DimensionWrapper.of(world))),
					Messenger.format("- %1$s: %2$s", tr("query.type"), Messenger.blockEntity(blockEntity)),
					Messenger.format("- %1$s: %2$s", tr("query.ticking_order"), index != -1 ? index : "N/A")
			));
			return 1;
		}
		else
		{
			Messenger.tell(source, tr("query.not_found", Messenger.coord(pos, DimensionWrapper.of(world))));
			return 0;
		}
	}

	private void showTopNInCollection(ServerCommandSource source, BaseText name, Collection<BlockEntity> blockEntities)
	{
		Multiset<BlockEntityType<?>> counter = HashMultiset.create();
		blockEntities.stream().map(BlockEntity::getType).forEach(counter::add);
		List<Multiset.Entry<BlockEntityType<?>>> topN = counter.entrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingInt(Multiset.Entry::getCount))).
				limit(TOP_N).
				collect(Collectors.toList());

		Messenger.tell(source, Messenger.format("%1$s: %2$sx", name, blockEntities.size()));
		for (int i = 0; i < topN.size(); i++)
		{
			Multiset.Entry<BlockEntityType<?>> entry = topN.get(i);
			Messenger.tell(source, Messenger.formatting(Messenger.format(
					"%1$s. %2$s: %3$sx",
					i + 1,
					IdentifierUtil.id(entry.getElement()),
					entry.getCount()
			), "g"));
		}
	}

	private int showStatistic(ServerCommandSource source)
	{
		ServerWorld world = source.getWorld();
		Messenger.tell(source, Messenger.format(
				"===== %1$s =====",
				tr("statistic.title", Messenger.dimension(DimensionWrapper.of(world)))
		));

		// query to all block entity is not available in 1.17+
		//#if MC < 11700
		//$$ this.showTopNInCollection(source, tr("statistic.all"), world.blockEntities);
		//#endif

		this.showTopNInCollection(
				source, tr("statistic.ticking"),
				//#if MC >= 11700
				getTickingBlockEntities(world)
				//#else
				//$$ world.tickingBlockEntities
				//#endif
		);
		return 1;
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode(CommandTreeContext context)
	{
		return super.getCommandNode(context).
				then(literal("statistic").
						executes(c -> this.showStatistic(c.getSource()))
				).
				then(literal("query").
						then(argument("pos", blockPos()).
								executes(c -> this.queryTileEntityInfo(c.getSource(), getLoadedBlockPos(c, "pos")))
						)
				);
	}
}
