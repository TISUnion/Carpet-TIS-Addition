package carpettisaddition.commands.info;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.mixins.command.info.ServerWorldAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
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
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.then(
				literal("world").
				then(literal("ticking_order").
						executes((c) -> showWorldTickOrder(c.getSource()))
				).
				then(literal("weather").
						executes((c) -> showWeather(c.getSource()))
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

	@SuppressWarnings("ConstantConditions")
	private int showWeather(ServerCommandSource source)
	{
		ServerWorld world = source.getWorld();
		LevelProperties worldInfo = world.getLevelProperties();

		int clearTime = worldInfo.getClearWeatherTime();
		boolean raining = worldInfo.isRaining();
		boolean thundering = worldInfo.isThundering();
		int rainTime = worldInfo.getRainTime();
		int thunderTime = worldInfo.getThunderTime();

		BiFunction<String, Object, BaseText> itemPrint = (key, value) -> Messenger.c(
				Messenger.hover(tr("weather.data." + key), Messenger.s(key)),
				Messenger.s(" = ", Formatting.GRAY),
				Messenger.colored(value)
		);
		Function<Integer, BaseText> toMin = ticks -> Messenger.c(
				Messenger.hover(Messenger.s(String.format("%.1f", (double)ticks / 20 / 60)), Messenger.s(ticks + " ticks")),
				"g min"
		);
		BiFunction<BaseText, Integer, BaseText> durationPrint = (name, ticks) -> Messenger.c(name, "g : ", toMin.apply(ticks));

		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, Messenger.c("g ======= ", tr("weather.data.title"), "g  ======="));
		Messenger.tell(source, itemPrint.apply("clearWeatherTime", clearTime));
		Messenger.tell(source, itemPrint.apply("rainTime", rainTime));
		Messenger.tell(source, itemPrint.apply("thunderTime", thunderTime));
		Messenger.tell(source, itemPrint.apply("raining", raining));
		Messenger.tell(source, itemPrint.apply("thundering", thundering));

		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, Messenger.c("g ======= ", tr("weather.forecast.title"), "g  ======="));
		if (clearTime > 0)
		{
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.clear_sky_duration"), clearTime));
		}
		else if (raining && thundering)
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.thundering"), Formatting.LIGHT_PURPLE)));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.rain_duration"), rainTime));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.thunder_duration"), Math.min(rainTime, thunderTime)));
		}
		else if (raining && !thundering)
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.raining"), Formatting.BLUE)));
			Messenger.tell(source, durationPrint.apply(tr("weather.forecast.rain_duration"), rainTime));
			if (thunderTime < rainTime)
			{
				Messenger.tell(source, tr("weather.forecast.thunder_in", toMin.apply(thunderTime)));
			}
			else
			{
				Messenger.tell(source, tr("weather.forecast.no_thunder"));
			}
		}
		else
		{
			Messenger.tell(source, tr("weather.forecast.current", Messenger.formatting(tr("weather.weathers.clear_sky"), Formatting.WHITE)));
			Messenger.tell(source, tr("weather.forecast.rain_in", toMin.apply(rainTime)));
			if (thundering)
			{
				if (rainTime < thunderTime)
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.with_thunder", toMin.apply(thunderTime - rainTime)));
				}
				else
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.thunder_unknown", toMin.apply(thunderTime)));
				}
			}
			else
			{
				if (rainTime < thunderTime)
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.no_thunder_for_now", toMin.apply(thunderTime)));
				}
				else
				{
					Messenger.tell(source, tr("weather.forecast.rain_in.maybe_thunder", toMin.apply(thunderTime)));
				}
			}
		}
		return 1;
	}

	private <T> void appendTileTickInfo(List<BaseText> result, List<ScheduledTick<T>> tileTickList, String title, long currentTime, Function<T, BaseText> nameGetter)
	{
		if (!tileTickList.isEmpty())
		{
			result.add(Messenger.s(String.format(" - %s * %d", title, tileTickList.size())));
			for (ScheduledTick<T> tt : tileTickList)
			{
				long time =
						//#if MC >= 11800
						//$$ tt.triggerTick();
						//#else
						tt.time;
						//#endif

				TickPriority priority =
						//#if MC >= 11800
						//$$ tt.priority();
						//#else
						tt.priority;
						//#endif

				result.add(Messenger.c(
						"w     ",
						nameGetter.apply(tt.getObject()),
						String.format("w : time = %d (+%dgt), priority = %d", time, time - currentTime, priority.getIndex())
				));
			}
		}
	}

	private void appendBlockEventInfo(List<BaseText> result, List<BlockAction> blockEvents)
	{
		if (!blockEvents.isEmpty())
		{
			result.add(Messenger.s(" - Queued Block Events * " + blockEvents.size()));
			for (BlockAction be : blockEvents)
			{
				result.add(Messenger.c(
						"w     ",
						Messenger.block(be.getBlock()),
						String.format("w : id = %d, param = %d", be.getType(), be.getData()))
				);
			}
		}
	}

	//#if MC >= 11800
	//$$ @SuppressWarnings("unchecked")
	//$$ private <T> List<OrderedTick<T>> getTileTicksAt(WorldTickScheduler<T> wts, BlockPos pos)
	//$$ {
	//$$ 	ChunkTickScheduler<T> cts = ((WorldTickSchedulerAccessor<T>)wts).getChunkTickSchedulers().get(ChunkPos.toLong(pos));
	//$$ 	if (cts != null)
	//$$ 	{
	//$$ 		Queue<OrderedTick<T>> queue = ((QueueAccessibleChunkTickScheduler<T>)cts).getTickQueue$TISCM();
	//$$ 		if (queue != null)
	//$$ 		{
	//$$ 			return queue.stream().filter(t -> t.pos().equals(pos)).sorted(OrderedTick.TRIGGER_TICK_COMPARATOR).collect(Collectors.toList());
	//$$ 		}
	//$$ 	}
	//$$ 	return Collections.emptyList();
	//$$ }
	//#endif

	public Collection<BaseText> showMoreBlockInfo(BlockPos pos, World world)
	{
		if (!(world instanceof ServerWorld))
		{
			return Collections.emptyList();
		}
		List<BaseText> result = Lists.newArrayList();

		//#if MC >= 11800
		//$$ List<OrderedTick<Block>> blockTileTicks = this.getTileTicksAt((WorldTickScheduler<Block>)world.getBlockTickScheduler(), pos);
		//$$ List<OrderedTick<Fluid>> liquidTileTicks = this.getTileTicksAt((WorldTickScheduler<Fluid>)world.getFluidTickScheduler(), pos);
		//#else
		BlockBox bound =
				//#if MC >= 11700
				//$$ BlockBox.create
				//#else
				new BlockBox
				//#endif
				(pos, pos.add(1, 1, 1));
		List<ScheduledTick<Block>> blockTileTicks = ((ServerTickScheduler<Block>)world.getBlockTickScheduler()).getScheduledTicks(bound, false, false);
		List<ScheduledTick<Fluid>> liquidTileTicks = ((ServerTickScheduler<Fluid>)world.getFluidTickScheduler()).getScheduledTicks(bound, false, false);
		//#endif

		this.appendTileTickInfo(result, blockTileTicks, "Block Tile ticks", world.getTime(), Messenger::block);
		this.appendTileTickInfo(result, liquidTileTicks, "Fluid Tile ticks", world.getTime(), Messenger::fluid);
		List<BlockAction> blockEvents = ((ServerWorldAccessor)world).getPendingBlockActions().stream().filter(be -> be.getPos().equals(pos)).collect(Collectors.toList());
		this.appendBlockEventInfo(result, blockEvents);
		return result;
	}
}
