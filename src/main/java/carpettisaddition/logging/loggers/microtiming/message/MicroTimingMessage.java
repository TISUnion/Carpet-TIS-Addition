package carpettisaddition.logging.loggers.microtiming.message;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import carpettisaddition.logging.loggers.microtiming.events.EventSource;
import carpettisaddition.logging.loggers.microtiming.tickstages.TickStageExtraBase;
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
import net.minecraft.world.dimension.DimensionType;

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

	private final DimensionType dimensionType;
	private final BlockPos pos;
	private final DyeColor color;
	private final TickStage stage;
	private final String stageDetail;
	private final TickStageExtraBase stageExtra;
	private final BaseText stackTraceText;
	private final BaseEvent event;
	private final String blockName;

	public MicroTimingMessage(MicroTimingLogger logger, MicroTimingContext context)
	{
		this.dimensionType = context.getWorld().getDimension().getType();
		this.pos = context.getBlockPos();
		this.color = context.getColor();
		this.event = context.getEventSupplier().get();
		this.blockName = context.getBlockName();
		this.stage = logger.getTickStage();
		this.stageDetail = logger.getTickStageDetail();
		this.stageExtra = logger.getTickStageExtra();
		this.stackTraceText = StackTracePrinter.create().ignore(MicroTimingLoggerManager.class).deobfuscate().toSymbolText();
	}

	public MessageType getMessageType()
	{
		return MessageType.fromEventType(event.getEventType());
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
				stage == message.stage &&
				Objects.equals(stageDetail, message.stageDetail) &&
				Objects.equals(stageExtra, message.stageExtra) &&
				Objects.equals(event, message.event);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dimensionType, pos, color, stage, stageDetail, stageExtra, event);
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

	private BaseText getStageText()
	{
		List<Object> stageText = Lists.newArrayList();
		stageText.add("y  " + this.stage.tr());
		if (this.stageDetail != null)
		{
			stageText.add("y ." + TRANSLATOR.tr("stage_detail." + this.stageDetail, this.stageDetail));
		}
		List<Object> hoverTextList = Lists.newArrayList();
		hoverTextList.add(this.stageExtra != null ? Messenger.c(this.stageExtra.toText(), "w \n"): Messenger.s(""));
		hoverTextList.add(String.format("w %s: ", TRANSLATOR.tr("Dimension")));
		hoverTextList.add(this.stage.isInsideWorld() ? TextUtil.getDimensionNameText(this.dimensionType) : "w N/A");
		return Messenger.c(
				"g  @",
				TextUtil.getFancyText(
						null,
						Messenger.c(stageText.toArray(new Object[0])),
						Messenger.c(hoverTextList.toArray(new Object[0])),
						this.stageExtra != null ? this.stageExtra.getClickEvent() : null
				)
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
						eventSource.getName(),
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
				line.add(this.getStageText());
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
