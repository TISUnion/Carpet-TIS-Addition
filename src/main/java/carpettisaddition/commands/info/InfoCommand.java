package carpettisaddition.commands.info;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.mixins.command.info.ServerWorldAccessor;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
	}

	public void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder)
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
					"g " + order + ". ",
					Messenger.dimension(DimensionWrapper.of(world))
			);
		}
		return 1;
	}

	private void appendTileTickInfo(List<BaseText> result, List<OrderedTick<?>> tileTickList, String title, long currentTime, Function<Object, BaseText> nameGetter)
	{
		if (!tileTickList.isEmpty())
		{
			result.add(Messenger.s(String.format(" - %s * %d", title, tileTickList.size())));
			for (OrderedTick<?> tt : tileTickList)
			{
				result.add(Messenger.c(
						"w     ",
						nameGetter.apply(tt.type()),
						String.format("w : time = %d (+%dgt), priority = %d", tt.triggerTick(), tt.triggerTick() - currentTime, tt.priority().getIndex())
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

	public Collection<BaseText> showMoreBlockInfo(BlockPos pos, World world)
	{
		if (!(world instanceof ServerWorld))
		{
			return Collections.emptyList();
		}
		List<BaseText> result = Lists.newArrayList();
		// TODO: make tiletick display work
//		BlockBox bound = BlockBox.create(pos, pos.add(1, 1, 1));
//		List<ScheduledTick<Block>> blockTileTicks = ((ServerTickScheduler<Block>)world.getBlockTickScheduler()).getScheduledTicks(bound, false, false);
//		List<ScheduledTick<Fluid>> liquidTileTicks = ((ServerTickScheduler<Fluid>)world.getFluidTickScheduler()).getScheduledTicks(bound, false, false);
//		this.appendTileTickInfo(result, blockTileTicks, "Block Tile ticks", world.getTime(), Messenger::block);
//		this.appendTileTickInfo(result, liquidTileTicks, "Fluid Tile ticks", world.getTime(), Messenger::fluid);
		List<BlockEvent> blockEvents = ((ServerWorldAccessor)world).getPendingBlockActions().stream().filter(be -> be.pos().equals(pos)).collect(Collectors.toList());
		this.appendBlockEventInfo(result, blockEvents);
		return result;
	}
}
