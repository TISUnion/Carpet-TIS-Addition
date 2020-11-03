package carpettisaddition.logging.loggers.microtiming;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.BaseLogger;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import carpettisaddition.logging.loggers.microtiming.events.BlockStateChangeEvent;
import carpettisaddition.logging.loggers.microtiming.message.IndentedMessage;
import carpettisaddition.logging.loggers.microtiming.message.MessageList;
import carpettisaddition.logging.loggers.microtiming.message.MessageType;
import carpettisaddition.logging.loggers.microtiming.message.MicroTimingMessage;
import carpettisaddition.logging.loggers.microtiming.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiFunction;

public class MicroTimingLogger extends BaseLogger
{
	// [stage][detail]^[extra]

	private TickStage stage;
	private String stageDetail;
	private TickStageExtraBase stageExtra;
	private final ServerWorld world;
	public final MessageList messageList = new MessageList();
	private final BaseText dimensionDisplayTextGray;

	public MicroTimingLogger(ServerWorld world)
	{
		super("microTiming");
		this.world = world;
		if (world != null)
		{
			this.dimensionDisplayTextGray = Util.getFancyText(
					"g",
					Util.getDimensionNameText(this.world.getDimension().getType()),
					Messenger.s(this.world.getDimension().getType().toString()),
					null
			);
		}
		else
		{
			this.dimensionDisplayTextGray = null;
		}
	}
	
	public void setTickStage(TickStage stage)
	{
		this.stage = stage;
		this.stageDetail = null;
		this.stageExtra = null;
	}

	public TickStage getTickStage()
	{
		return this.stage;
	}

	public void setTickStageDetail(String stageDetail)
	{
		this.stageDetail = stageDetail;
	}

	public String getTickStageDetail()
	{
		return this.stageDetail;
	}

	public void setTickStageExtra(TickStageExtraBase extra)
	{
		this.stageExtra = extra;
	}

	public TickStageExtraBase getTickStageExtra()
	{
		return this.stageExtra;
	}

	public ServerWorld getWorld()
	{
		return this.world;
	}

	private final static Set<Property<?>> INTEREST_PROPERTIES = new ReferenceArraySet<>();
	static
	{
		INTEREST_PROPERTIES.add(Properties.POWERED);
		INTEREST_PROPERTIES.add(Properties.LIT);
		INTEREST_PROPERTIES.add(Properties.POWER);
	}

	public void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, EventType eventType)
	{
		// lazy loading
		DyeColor color = null;
		BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, returnValue, newState.getBlock());

		for (Property<?> property: newState.getProperties())
		{
			if (INTEREST_PROPERTIES.contains(property))
			{
				if (color == null)
				{
					Optional<DyeColor> optionalDyeColor = MicroTimingUtil.getWoolOrEndRodWoolColor(world, pos);
					if (!optionalDyeColor.isPresent())
					{
						break;
					}
					color = optionalDyeColor.get();
				}
				event.addChanges(property.getName(), oldState.get(property), newState.get(property));
			}
		}
		if (event.hasChanges())
		{
			this.addMessage(color, world, pos, event);
		}
	}

	public void addMessage(DyeColor color, World world, BlockPos pos, BaseEvent event)
	{
		MicroTimingMessage message = new MicroTimingMessage(this, world.getDimension().getType(), pos, color, event);
		if (message.getEvent().getEventType() != EventType.ACTION_END)
		{
			this.messageList.addMessageAndIndent(message);
		}
		else
		{
			this.messageList.addMessageAndUnIndent(message);
		}
	}

	public void addMessage(World world, BlockPos pos, BaseEvent event, BiFunction<World, BlockPos, Optional<DyeColor>> woolGetter)
	{
		Optional<DyeColor> color = woolGetter.apply(world, pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, world, pos, event));
	}

	public void addMessage(World world, BlockPos pos, BaseEvent event)
	{
		this.addMessage(world, pos, event, MicroTimingUtil::getWoolOrEndRodWoolColor);
	}

	private BaseText getMergedResult(int count, IndentedMessage previousMessage)
	{
		return Messenger.c(
				MicroTimingMessage.getIndentationText(previousMessage.getIndentation()),
				Util.getFancyText(
						"g",
						Messenger.s(String.format("  +%dx", count)),
						Messenger.c(
								String.format("w %s\n", String.format(this.tr("merged_message", "Merged %d more same message" + (count > 1 ? "s" : "")), count)),
								previousMessage.getMessage().toText(0, true)
						),
						null
				)
		);
	}

	private BaseText[] getTrimmedMessages(List<IndentedMessage> flushedMessages, LoggingOption option)
	{
		List<BaseText> msg = Lists.newArrayList();
		Set<MicroTimingMessage> messageHashSet = Sets.newHashSet();
		msg.add(Messenger.s(" "));
		msg.add(Messenger.c(
				"f [",
				"f " + this.tr("GameTime"),
				"^w world.getTime()",
				"g  " + this.world.getTime(),
				"f  @ ",
				this.dimensionDisplayTextGray,
				"f ] ------------"
		));
		int skipCount = 0;
		Iterator<IndentedMessage> iterator = flushedMessages.iterator();
		IndentedMessage previousMessage = null;
		while (iterator.hasNext())
		{
			IndentedMessage message = iterator.next();
			boolean showThisMessage = option == LoggingOption.ALL || message.getMessage().getMessageType() == MessageType.PROCEDURE;
			if (!showThisMessage && option == LoggingOption.MERGED)
			{
				showThisMessage = previousMessage == null || !message.getMessage().equals(previousMessage.getMessage());
			}
			if (!showThisMessage && option == LoggingOption.UNIQUE)
			{
				showThisMessage = messageHashSet.add(message.getMessage());
			}
			if (showThisMessage)
			{
				if (option == LoggingOption.MERGED && previousMessage != null && skipCount > 0)
				{
					msg.add(this.getMergedResult(skipCount, previousMessage));
				}
				msg.add(message.toText());
				previousMessage = message;
				skipCount = 0;
			}
			else
			{
				skipCount++;
			}
			if (!iterator.hasNext() && option == LoggingOption.MERGED && skipCount > 0)
			{
				msg.add(this.getMergedResult(skipCount, previousMessage));
			}
		}
		return msg.toArray(new BaseText[0]);
	}

	public void flushMessages()
	{
		if (!this.messageList.isEmpty())
		{
			List<IndentedMessage> flushedMessages = this.messageList.flush();
			if (!flushedMessages.isEmpty())
			{
				Map<LoggingOption, BaseText[]> flushedTrimmedMessages = new EnumMap<>(LoggingOption.class);
				for (LoggingOption option : LoggingOption.values())
				{
					flushedTrimmedMessages.put(option, getTrimmedMessages(flushedMessages, option));
				}
				LoggerRegistry.getLogger("microTiming").log((option) -> flushedTrimmedMessages.get(LoggingOption.ofString(option)));
			}
		}
	}

	public enum LoggingOption
	{
		MERGED,
		ALL,
		UNIQUE;

		public static final LoggingOption DEFAULT = LoggingOption.MERGED;
		private static final Map<String, LoggingOption> OPTION_MAP = new Object2ObjectArrayMap<>();

		static
		{
			for (LoggingOption option : LoggingOption.values())
			{
				OPTION_MAP.put(option.name(), option);
				OPTION_MAP.put(option.name().toLowerCase(), option);
			}
		}

		public static LoggingOption ofString(String str)
		{
			return OPTION_MAP.getOrDefault(str, DEFAULT);
		}
	}
}
