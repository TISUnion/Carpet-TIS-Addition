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
import carpettisaddition.commands.info.world.InfoWorldCommand;
import carpettisaddition.mixins.command.info.ServerWorldAccessor;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.literal;

//#if MC >= 11800
import carpettisaddition.mixins.command.info.ChunkTickSchedulerAccessor;
import carpettisaddition.mixins.command.info.WorldTickSchedulerAccessor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import java.util.Queue;
//#else
//$$ import net.minecraft.server.world.ServerTickScheduler;
//$$ import net.minecraft.util.math.BlockBox;
//$$ import net.minecraft.world.ScheduledTick;
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
			LiteralArgumentBuilder<ServerCommandSource> worldNode = literal(name);
			subcommand.extendCommand(context.node(worldNode));
			context.node.then(worldNode);
		};
		extend.accept("world", InfoWorldCommand.getInstance());
	}

	private <T> void appendTileTickInfo(List<BaseText> result, List<OrderedTick<T>> tileTickList, String title, long currentTime, Function<T, BaseText> nameGetter)
	{
		if (!tileTickList.isEmpty())
		{
			result.add(Messenger.s(String.format(" - %s * %d", title, tileTickList.size())));
			for (OrderedTick<T> tt : tileTickList)
			{
				long time =
						//#if MC >= 11800
						tt.triggerTick();
						//#else
						//$$ tt.time;
						//#endif

				TickPriority priority =
						//#if MC >= 11800
						tt.priority();
						//#else
						//$$ tt.priority;
						//#endif

				result.add(Messenger.c(
						"w     ",
						nameGetter.apply(tt.type()),
						String.format("w : time = %d (+%dgt), priority = %d", time, time - currentTime, priority.getIndex())
				));
			}
		}
	}

	private void appendBlockEventInfo(List<BaseText> result, List<BlockEvent> blockEvents)
	{
		if (!blockEvents.isEmpty())
		{
			result.add(Messenger.s(" - Queued Block Events * " + blockEvents.size()));
			for (BlockEvent be : blockEvents)
			{
				result.add(Messenger.c(
						"w     ",
						Messenger.block(be.block()),
						String.format("w : id = %d, param = %d", be.type(), be.data()))
				);
			}
		}
	}

	//#if MC >= 11800
	@SuppressWarnings("unchecked")
	private <T> List<OrderedTick<T>> getTileTicksAt(WorldTickScheduler<T> wts, BlockPos pos)
	{
		ChunkTickScheduler<T> cts = ((WorldTickSchedulerAccessor<T>)wts).getChunkTickSchedulers().get(ChunkPos.toLong(pos));
		if (cts != null)
		{
			Queue<OrderedTick<T>> queue = ((QueueAccessibleChunkTickScheduler<T>)cts).getTickQueue$TISCM();
			if (queue != null)
			{
				return queue.stream().filter(t -> t.pos().equals(pos)).sorted(OrderedTick.TRIGGER_TICK_COMPARATOR).collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}
	//#endif

	public Collection<BaseText> showMoreBlockInfo(BlockPos pos, World world)
	{
		if (!(world instanceof ServerWorld))
		{
			return Collections.emptyList();
		}
		List<BaseText> result = Lists.newArrayList();

		//#if MC >= 11800
		List<OrderedTick<Block>> blockTileTicks = this.getTileTicksAt((WorldTickScheduler<Block>)world.getBlockTickScheduler(), pos);
		List<OrderedTick<Fluid>> liquidTileTicks = this.getTileTicksAt((WorldTickScheduler<Fluid>)world.getFluidTickScheduler(), pos);
		//#else
		//$$ BlockBox bound =
				//#if MC >= 11700
				//$$ BlockBox.create
				//#else
				//$$ new BlockBox
				//#endif
		//$$ 		(pos, pos.add(1, 1, 1));
		//$$ List<ScheduledTick<Block>> blockTileTicks = ((ServerTickScheduler<Block>)world.getBlockTickScheduler()).getScheduledTicks(bound, false, false);
		//$$ List<ScheduledTick<Fluid>> liquidTileTicks = ((ServerTickScheduler<Fluid>)world.getFluidTickScheduler()).getScheduledTicks(bound, false, false);
		//#endif

		this.appendTileTickInfo(result, blockTileTicks, "Block Tile ticks", world.getTime(), Messenger::block);
		this.appendTileTickInfo(result, liquidTileTicks, "Fluid Tile ticks", world.getTime(), Messenger::fluid);
		List<BlockEvent> blockEvents = ((ServerWorldAccessor)world).getPendingBlockActions().stream().filter(be -> be.pos().equals(pos)).collect(Collectors.toList());
		this.appendBlockEventInfo(result, blockEvents);
		return result;
	}
}
