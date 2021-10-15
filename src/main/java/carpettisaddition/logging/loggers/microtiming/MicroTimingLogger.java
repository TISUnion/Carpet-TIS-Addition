package carpettisaddition.logging.loggers.microtiming;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.logging.loggers.microtiming.message.IndentedMessage;
import carpettisaddition.logging.loggers.microtiming.message.MessageList;
import carpettisaddition.logging.loggers.microtiming.message.MessageType;
import carpettisaddition.logging.loggers.microtiming.message.MicroTimingMessage;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.AbstractSubStage;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingContext;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MicroTimingLogger extends AbstractLogger
{
	// [stage][detail]^[extra]
	public static final String NAME = "microTiming";

	private TickPhase tickPhase;
	private final ServerWorld world;
	public final MessageList messageList = new MessageList();

	public MicroTimingLogger(@NotNull ServerWorld world)
	{
		super(NAME);
		this.world = world;
		this.tickPhase = new TickPhase(TickStage.UNKNOWN, this.world.getRegistryKey());
	}
	
	public void setTickStage(@NotNull TickStage stage)
	{
		this.tickPhase = this.tickPhase.withMainStage(stage);
	}

	public void setTickStageDetail(String stageDetail)
	{
		this.tickPhase = this.tickPhase.withDetailed(stageDetail);
	}

	public void setSubTickStage(AbstractSubStage subStage)
	{
		this.tickPhase = this.tickPhase.withSubStage(subStage);
	}

	public TickPhase getTickPhase()
	{
		return this.tickPhase;
	}

	public ServerWorld getWorld()
	{
		return this.world;
	}

	public void addMessage(MicroTimingContext context)
	{
		if (context.getColor() == null)
		{
			if (context.getWoolGetter() == null)
			{
				context.withWoolGetter(MicroTimingUtil::defaultColorGetter);
			}
			Optional<DyeColor> optionalDyeColor = context.getWoolGetter().apply(this.world, context.getBlockPos());
			if (optionalDyeColor.isPresent())
			{
				context.withColor(optionalDyeColor.get());
			}
			else
			{
				return;
			}
		}
		MicroTimingMarkerManager.getInstance().getMarkerName(context.getWorld(), context.getBlockPos()).ifPresent(context::withBlockName);
		MicroTimingMessage message = new MicroTimingMessage(this, context);
		if (message.getEvent().getEventType() != EventType.ACTION_END)
		{
			this.messageList.addMessageAndIndent(message);
		}
		else
		{
			this.messageList.addMessageAndUnIndent(message);
		}
	}

	private BaseText getMergedResult(int count, IndentedMessage previousMessage)
	{
		return Messenger.c(
				MicroTimingMessage.getIndentationText(previousMessage.getIndentation()),
				TextUtil.getFancyText(
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
				TextUtil.getFancyText(
						"g",
						TextUtil.getDimensionNameText(this.world.getRegistryKey()),
						Messenger.s(this.world.getRegistryKey().getValue().toString()),
						null
				),
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
				LoggerRegistry.getLogger(NAME).log(option -> flushedTrimmedMessages.get(LoggingOption.getOrDefault(option)));
			}
		}
	}

	public enum LoggingOption
	{
		MERGED,
		ALL,
		UNIQUE;

		public static final LoggingOption DEFAULT = MERGED;

		public static LoggingOption getOrDefault(String option)
		{
			LoggingOption loggingOption;
			try
			{
				loggingOption = LoggingOption.valueOf(option.toUpperCase());
			}
			catch (IllegalArgumentException e)
			{
				loggingOption = LoggingOption.DEFAULT;
			}
			return loggingOption;
		}
	}
}
