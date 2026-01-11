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

package carpettisaddition.commands.info;

import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.info.server.InfoServerCommand;
import carpettisaddition.commands.info.world.InfoWorldCommand;
import carpettisaddition.mixins.command.info.ServerWorldAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.WorldUtils;
import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.literal;

//#if MC >= 11800
//$$ import carpettisaddition.mixins.command.info.ChunkTickSchedulerAccessor;
//$$ import carpettisaddition.mixins.command.info.WorldTickSchedulerAccessor;
//$$ import carpettisaddition.utils.PositionUtils;
//$$ import net.minecraft.world.level.ChunkPos;
//$$ import net.minecraft.world.ticks.LevelChunkTicks;
//$$ import net.minecraft.world.ticks.ScheduledTick;
//$$ import net.minecraft.world.ticks.LevelTicks;
//$$ import java.util.Queue;
//#else
import net.minecraft.world.level.ServerTickList;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.TickNextTickData;
//#endif

public class InfoCommandExtension extends AbstractCommand implements CommandExtender
{
	private static final InfoCommandExtension INSTANCE = new InfoCommandExtension();

	public static InfoCommandExtension getInstance()
	{
		return INSTANCE;
	}

	public InfoCommandExtension()
	{
		super("info");
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		BiConsumer<String, InfoSubcommand> extend = (name, subcommand) -> {
			LiteralArgumentBuilder<CommandSourceStack> childNode = literal(name);
			subcommand.extendCommand(context.node(childNode));
			context.node.then(childNode);
		};
		extend.accept("server", InfoServerCommand.getInstance());
		extend.accept("world", InfoWorldCommand.getInstance());
	}

	private <T> void appendTileTickInfo(List<BaseComponent> result, List<TickNextTickData<T>> tileTickList, String title, long currentTime, Function<T, BaseComponent> nameGetter)
	{
		if (!tileTickList.isEmpty())
		{
			result.add(Messenger.s(String.format(" - %s * %d", title, tileTickList.size())));
			for (TickNextTickData<T> tt : tileTickList)
			{
				long time =
						//#if MC >= 11800
						//$$ tt.triggerTick();
						//#else
						tt.delay;
						//#endif

				TickPriority priority =
						//#if MC >= 11800
						//$$ tt.priority();
						//#else
						tt.priority;
						//#endif

				result.add(Messenger.c(
						"w     ",
						nameGetter.apply(tt.getType()),
						String.format("w : time = %d (+%dgt), priority = %d", time, time - currentTime, priority.getValue())
				));
			}
		}
	}

	private void appendBlockEventInfo(List<BaseComponent> result, List<BlockEventData> blockEvents)
	{
		if (!blockEvents.isEmpty())
		{
			result.add(Messenger.s(" - Queued Block Events * " + blockEvents.size()));
			for (BlockEventData be : blockEvents)
			{
				result.add(Messenger.c(
						"w     ",
						Messenger.block(be.getBlock()),
						String.format("w : id = %d, param = %d", be.getParamA(), be.getParamB()))
				);
			}
		}
	}

	//#if MC >= 11800
	//$$ @SuppressWarnings("unchecked")
	//$$ private <T> List<ScheduledTick<T>> getTileTicksAt(LevelTicks<T> wts, BlockPos pos)
	//$$ {
	//$$ 	LevelChunkTicks<T> cts = ((WorldTickSchedulerAccessor<T>)wts).getChunkTickSchedulers().get(PositionUtils.packChunkPos(pos));
	//$$ 	if (cts != null)
	//$$ 	{
	//$$ 		Queue<ScheduledTick<T>> queue = ((QueueAccessibleChunkTickScheduler<T>)cts).getTickQueue$TISCM();
	//$$ 		if (queue != null)
	//$$ 		{
	//$$ 			return queue.stream().filter(t -> t.pos().equals(pos)).sorted(ScheduledTick.DRAIN_ORDER).collect(Collectors.toList());
	//$$ 		}
	//$$ 	}
	//$$ 	return Collections.emptyList();
	//$$ }
	//#endif

	public Collection<BaseComponent> showMoreBlockInfo(BlockPos pos, Level world)
	{
		if (!(world instanceof ServerLevel))
		{
			return Collections.emptyList();
		}
		List<BaseComponent> result = Lists.newArrayList();

		//#if MC >= 11800
		//$$ List<ScheduledTick<Block>> blockTileTicks = this.getTileTicksAt((LevelTicks<Block>)world.getBlockTicks(), pos);
		//$$ List<ScheduledTick<Fluid>> liquidTileTicks = this.getTileTicksAt((LevelTicks<Fluid>)world.getFluidTicks(), pos);
		//#else
		BoundingBox bound =
				//#if MC >= 11700
				//$$ BoundingBox.fromCorners
				//#else
				new BoundingBox
				//#endif
				(pos, pos.offset(1, 1, 1));
		List<TickNextTickData<Block>> blockTileTicks = ((ServerTickList<Block>)world.getBlockTicks()).fetchTicksInArea(bound, false, false);
		List<TickNextTickData<Fluid>> liquidTileTicks = ((ServerTickList<Fluid>)world.getLiquidTicks()).fetchTicksInArea(bound, false, false);
		//#endif

		this.appendTileTickInfo(result, blockTileTicks, "Block Tile ticks", WorldUtils.getWorldTime(world), Messenger::block);
		this.appendTileTickInfo(result, liquidTileTicks, "Fluid Tile ticks", WorldUtils.getWorldTime(world), Messenger::fluid);
		List<BlockEventData> blockEvents = ((ServerWorldAccessor)world).getPendingBlockActions().stream().filter(be -> be.getPos().equals(pos)).collect(Collectors.toList());
		this.appendBlockEventInfo(result, blockEvents);
		return result;
	}
}
