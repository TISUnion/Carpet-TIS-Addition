package carpettisaddition.commands.info;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.mixins.command.info.ChunkTickSchedulerAccessor;
import carpettisaddition.mixins.command.info.ServerWorldAccessor;
import carpettisaddition.mixins.command.info.WorldTickSchedulerAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.literal;

public class InfoCommand extends AbstractCommand implements CommandExtender
{
	private static final InfoCommand INSTANCE = new InfoCommand();

	public static InfoCommand getInstance()
	{
		return INSTANCE;
	}

	public InfoCommand()
	{
		super("info");
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext)
	{
	}

	public void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder, CommandRegistryAccess commandBuildContext)
	{
		builder.then(
				literal("world").
				then(
						literal("ticking_order").
						executes((c) -> showWorldTickOrder(c.getSource()))
				)
		);
	}

	private int showWorldTickOrder(ServerCommandSource source)
	{
		List<World> worlds = Lists.newArrayList(CarpetTISAdditionServer.minecraft_server.getWorlds());
		Messenger.tell(source, tr("ticking_order", worlds.size()));
		int order = 0;
		for (World world : worlds)
		{
			order++;
			Messenger.tell(
					source,
					Messenger.c(
							"g " + order + ". ",
							Messenger.dimension(DimensionWrapper.of(world))
					)
			);
		}
		return 1;
	}

	private <T> void appendTileTickInfo(List<MutableText> result, List<OrderedTick<T>> tileTickList, String title, long currentTime, Function<T, MutableText> nameGetter)
	{
		if (!tileTickList.isEmpty())
		{
			result.add(Messenger.s(String.format(" - %s * %d", title, tileTickList.size())));
			for (OrderedTick<T> tt : tileTickList)
			{
				result.add(Messenger.c(
						"w     ",
						nameGetter.apply(tt.type()),
						String.format("w : time = %d (+%dgt), priority = %d", tt.triggerTick(), tt.triggerTick() - currentTime, tt.priority().getIndex())
				));
			}
		}
	}

	private void appendBlockEventInfo(List<MutableText> result, List<BlockEvent> blockEvents)
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

	@SuppressWarnings("unchecked")
	private <T> List<OrderedTick<T>> getTileTicksAt(WorldTickScheduler<T> wts, BlockPos pos)
	{
		ChunkTickScheduler<T> cts = ((WorldTickSchedulerAccessor<T>)wts).getChunkTickSchedulers().get(ChunkPos.toLong(pos));
		if (cts != null)
		{
			Queue<OrderedTick<T>> queue = ((ChunkTickSchedulerAccessor<T>)cts).getTickQueue();
			return queue.stream().filter(t -> t.pos().equals(pos)).sorted(OrderedTick.TRIGGER_TICK_COMPARATOR).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	public Collection<MutableText> showMoreBlockInfo(BlockPos pos, World world)
	{
		if (!(world instanceof ServerWorld))
		{
			return Collections.emptyList();
		}
		List<MutableText> result = Lists.newArrayList();
		List<OrderedTick<Block>> blockTileTicks = this.getTileTicksAt((WorldTickScheduler<Block>)world.getBlockTickScheduler(), pos);
		List<OrderedTick<Fluid>> liquidTileTicks = this.getTileTicksAt((WorldTickScheduler<Fluid>)world.getFluidTickScheduler(), pos);
		this.appendTileTickInfo(result, blockTileTicks, "Block Tile ticks", world.getTime(), Messenger::block);
		this.appendTileTickInfo(result, liquidTileTicks, "Fluid Tile ticks", world.getTime(), Messenger::fluid);
		List<BlockEvent> blockEvents = ((ServerWorldAccessor)world).getPendingBlockActions().stream().filter(be -> be.pos().equals(pos)).collect(Collectors.toList());
		this.appendBlockEventInfo(result, blockEvents);
		return result;
	}
}
