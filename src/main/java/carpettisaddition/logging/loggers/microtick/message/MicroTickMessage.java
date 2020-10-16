package carpettisaddition.logging.loggers.microtick.message;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import carpettisaddition.logging.loggers.microtick.enums.TickStage;
import carpettisaddition.logging.loggers.microtick.events.BaseEvent;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.utils.StackTraceDeobfuscator;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.min;

public class MicroTickMessage
{
	private static final int MAX_STACK_TRACE_SIZE = 64;
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
	private final StackTraceElement[] stackTrace;
	private final BaseEvent event;

	public MicroTickMessage(DimensionType dimensionType, BlockPos pos, DyeColor color, BaseEvent event, TickStage stage, String stageDetail, TickStageExtraBase stageExtra, StackTraceElement[] stackTrace)
	{
		this.dimensionType = dimensionType;
		this.pos = pos.toImmutable();
		this.color = color;
		this.event = event;
		this.stage = stage;
		this.stageDetail = stageDetail;
		this.stageExtra = stageExtra;
		this.stackTrace = stackTrace;
	}
	public MicroTickMessage(MicroTickLogger logger, DimensionType dimensionType, BlockPos pos, DyeColor color, BaseEvent event)
	{
		this(dimensionType, pos, color, event, logger.getTickStage(), logger.getTickStageDetail(), logger.getTickStageExtra(), StackTraceDeobfuscator.deobfuscateStackTrace((new Exception(logger.getClass().getName())).getStackTrace()));
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
		if (!(o instanceof MicroTickMessage)) return false;
		MicroTickMessage message = (MicroTickMessage) o;
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

	private BaseText getHashTagText()
	{
		String text = MicroTickUtil.getColorStyle(this.color) + " # ";
		BaseText ret;
		if (this.pos != null)
		{
			ret = Messenger.c(
					text,
					"?" + Util.getTeleportCommand(pos, this.dimensionType),
					String.format("^w [ %d, %d, %d ]", this.pos.getX(), this.pos.getY(), this.pos.getZ())
			);
		}
		else
		{
			ret = Messenger.c(text);
		}
		return ret;
	}

	private BaseText getStageText()
	{
		List<Object> stageText = Lists.newArrayList();
		stageText.add("y  " + this.stage.tr());
		if (this.stageDetail != null)
		{
			stageText.add("y ." + MicroTickLoggerManager.tr("stage_detail." + this.stageDetail, this.stageDetail));
		}
		List<Object> hoverTextList = Lists.newArrayList();
		hoverTextList.add(this.stageExtra != null ? Messenger.c(this.stageExtra.toText(), "w \n"): Messenger.s(""));
		hoverTextList.add(String.format("w %s: ", MicroTickLoggerManager.tr("Dimension")));
		hoverTextList.add(this.stage.isInsideWorld() ? Util.getDimensionNameText(this.dimensionType) : "w N/A");
		return Messenger.c(
				"g  @",
				Util.getFancyText(
						null,
						Messenger.c(stageText.toArray(new Object[0])),
						Messenger.c(hoverTextList.toArray(new Object[0])),
						this.stageExtra != null ? this.stageExtra.getClickEvent() : null
				)
		);
	}

	private BaseText getStackTraceText()
	{
		List<StackTraceElement> list = Arrays.asList(this.stackTrace).subList(0, min(this.stackTrace.length, MAX_STACK_TRACE_SIZE));
		int restLineCount = this.stackTrace.length - MAX_STACK_TRACE_SIZE;
		String info = restLineCount > 0 ? String.format("\n+%d more lines", restLineCount) : "";
		return Messenger.c(
				"f  $",
				"^w " + Joiner.on("\n").join(list) + info
		);
	}

	public BaseText toText(int indentation, boolean showStage)
	{
		List<Object> line = Lists.newArrayList();
		if (indentation > 0)
		{
			line.add("w " + INDENTATIONS.get(min(indentation, MAX_INDENT)));
		}
		line.add(this.getHashTagText());
		line.add(event.toText());
		if (this.event.getEventType() != EventType.ACTION_END)
		{
			if (showStage)
			{
				line.add(this.getStageText());
			}
		}
		line.add(this.getStackTraceText());
		return Messenger.c(line.toArray(new Object[0]));
	}

	public void mergeQuiteMessage(MicroTickMessage quiteMessage)
	{
		if (quiteMessage != null)
		{
			this.event.mergeQuitEvent(quiteMessage.event);
		}
	}
}
