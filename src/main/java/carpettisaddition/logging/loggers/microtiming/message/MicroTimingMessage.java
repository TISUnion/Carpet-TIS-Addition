package carpettisaddition.logging.loggers.microtiming.message;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import carpettisaddition.logging.loggers.microtiming.events.EventSource;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingContext;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static java.lang.Integer.min;

public class MicroTimingMessage
{
	private static final Translator TRANSLATOR = MicroTimingLoggerManager.TRANSLATOR;

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

	private final RegistryKey<World> dimensionType;
	private final BlockPos pos;
	private final DyeColor color;
	private final TickPhase tickPhase;
	private final BaseText stackTraceText;
	private final BaseEvent event;
	private final String blockName;

	public MicroTimingMessage(MicroTimingLogger logger, MicroTimingContext context)
	{
		this.dimensionType = context.getWorld().getRegistryKey();
		this.pos = context.getBlockPos();
		this.color = context.getColor();
		this.event = context.getEventSupplier().get();
		this.blockName = context.getBlockName();
		this.tickPhase = logger.getTickPhase();
		this.stackTraceText = StackTracePrinter.create().ignore(MicroTimingLoggerManager.class).deobfuscate().toSymbolText();
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
		return TextUtil.getFancyText(
				MicroTimingUtil.getColorStyle(this.color),
				Messenger.s("# "),
				Messenger.s(
						String.format("%s: %s\n%s: %s\n%s: %s",
								TRANSLATOR.tr("Position"),
								TextUtil.getCoordinateString(this.pos),
								TRANSLATOR.tr("Color"),
								this.color,
								TRANSLATOR.tr("Indentation"),
								indentation
						)
				),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.pos, this.dimensionType))
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
		String type = "unknown";
		if (eventSource.getSourceObject() instanceof Block)
		{
			type = TRANSLATOR.tr("block");
		}
		else if (eventSource.getSourceObject() instanceof Fluid)
		{
			type = TRANSLATOR.tr("fluid");
		}
		return Messenger.c(
				"g [",
				TextUtil.getFancyText(
						null,
						this.blockName != null ? Messenger.s(this.blockName) : eventSource.getName(),
						Messenger.c(
								String.format("w %s: ", TRANSLATOR.tr("Event source")),
								eventSource.getName(),
								String.format("w  (%s)\n%s: %s", type, TRANSLATOR.tr("ID"), eventSource.getId())
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
