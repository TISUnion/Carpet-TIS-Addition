package carpettisaddition.logging.loggers.microtiming.message;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import carpettisaddition.logging.loggers.microtiming.events.EventSource;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingContext;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;

import static java.lang.Integer.min;

public class MicroTimingMessage
{
	private static final int MAX_INDENT = 10;
	private static final int SPACE_PER_INDENT = 2;
	private static final List<String> INDENTATIONS = Lists.newArrayList();
	static
	{
		String indent = "";
		for (int i = 0; i <= MAX_INDENT; i++)
		{
			INDENTATIONS.add(indent);
			for (int j = 0; j < SPACE_PER_INDENT; j++)
			{
				indent += ' ';
			}
		}
	}

	private final DimensionWrapper dimensionType;
	private final BlockPos pos;
	private final DyeColor color;
	private final TickPhase tickPhase;
	private final BaseText stackTraceText;
	private final BaseEvent event;
	private final String blockName;

	public MicroTimingMessage(MicroTimingLogger logger, MicroTimingContext context)
	{
		this.dimensionType = DimensionWrapper.of(context.getWorld());
		this.pos = context.getBlockPos();
		this.color = context.getColor();
		this.event = context.getEventSupplier().get();
		this.blockName = context.getBlockName();
		this.tickPhase = logger.getTickPhase();
		this.stackTraceText = StackTracePrinter.makeSymbol(MicroTimingLoggerManager.class);
	}

	private static BaseText tr(String key, Object... args)
	{
		return MicroTimingLoggerManager.TRANSLATOR.tr(key, args);
	}

	public MessageType getMessageType()
	{
		return MessageType.fromEventType(this.event.getEventType());
	}

	public BaseEvent getEvent()
	{
		return this.event;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof MicroTimingMessage)) return false;
		MicroTimingMessage message = (MicroTimingMessage) o;
		return Objects.equals(dimensionType, message.dimensionType) &&
				Objects.equals(pos, message.pos) &&
				color == message.color &&
				Objects.equals(tickPhase, message.tickPhase) &&
				Objects.equals(event, message.event);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dimensionType, pos, color, tickPhase, event);
	}

	private BaseText getHashTagText(int indentation)
	{
		return Messenger.fancy(
				MicroTimingUtil.getColorStyle(this.color),
				Messenger.s("# "),
				Messenger.c(
						tr("common.position"), Messenger.s(": "), Messenger.coord(this.pos), Messenger.newLine(),
						tr("common.color"), Messenger.s(": "), Messenger.s(this.color.toString()), Messenger.newLine(),
						tr("common.indentation"), Messenger.s(": "), Messenger.s(String.valueOf(indentation))
				),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.pos, this.dimensionType))
		);
	}

	public static BaseText getIndentationText(int indentation)
	{
		return Messenger.s(INDENTATIONS.get(min(indentation, MAX_INDENT)));
	}

	// [Stone]
	private BaseText getEnclosedTranslatedBlockNameHeaderText()
	{
		EventSource eventSource = this.event.getEventSource();
		BaseText type = Messenger.s("unknown");
		if (eventSource.getSourceObject() instanceof Block)
		{
			type = tr("common.block");
		}
		else if (eventSource.getSourceObject() instanceof Fluid)
		{
			type = tr("common.fluid");
		}
		return Messenger.c(
				"g [",
				Messenger.fancy(
						null,
						this.blockName != null ? Messenger.s(this.blockName) : eventSource.getName(),
						Messenger.c(
								tr("common.event_source"), "w : ",
								eventSource.getName(),
								"w  (", type, "w )\n",
								tr("common.id"), String.format("w : %s", eventSource.getId())
						),
						null
				),
				"g ] "
		);
	}

	public BaseText toText(int indentation, boolean showStage)
	{
		List<Object> line = Lists.newArrayList();
		if (indentation > 0)
		{
			line.add(getIndentationText(indentation));
		}
		line.add(this.getHashTagText(indentation));
		line.add(this.getEnclosedTranslatedBlockNameHeaderText());
		line.add(this.event.toText());
		if (this.event.getEventType() != EventType.ACTION_END)
		{
			if (showStage)
			{
				line.add(this.tickPhase.toText());
			}
		}
		line.add("w  ");
		line.add(this.stackTraceText);
		return Messenger.c(line.toArray(new Object[0]));
	}

	/**
	 * Merge the closing bracket event into current event, for cleaner view
	 *
	 * this (opening bracket)
	 *   sub message A
	 *     sub-sub message B
	 * quitMessage (closing bracket)
	 */
	public void mergeQuitMessage(MicroTimingMessage quitMessage)
	{
		if (quitMessage != null)
		{
			this.event.mergeQuitEvent(quitMessage.event);
		}
	}
}
