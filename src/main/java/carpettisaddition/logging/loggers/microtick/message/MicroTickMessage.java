package carpettisaddition.logging.loggers.microtick.message;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.events.BaseEvent;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.utils.StackTraceDeobfuscator;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;
import java.util.Objects;

import static java.lang.Integer.min;

public class MicroTickMessage
{
	private static final int MAX_INDENT = 8;
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
	private final String stage, stageDetail;
	private final ToTextAble stageExtra;
	private final StackTraceElement[] stackTrace;
	private final BaseEvent event;

	public MicroTickMessage(DimensionType dimensionType, BlockPos pos, DyeColor color, BaseEvent event, String stage, String stageDetail, ToTextAble stageExtra, StackTraceElement[] stackTrace)
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

	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof MicroTickMessage))
		{
			return false;
		}

		MicroTickMessage o = (MicroTickMessage) obj;
		return this.dimensionType.equals(o.dimensionType) && this.pos.equals(o.pos) && this.color.equals(o.color) && this.stage.equals(o.stage) && this.event.equals(o.event);
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
					String.format("!/execute in %s run tp @s %d %d %d", this.dimensionType, this.pos.getX(), this.pos.getY(), this.pos.getZ()),
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
		List<Object> comps = Lists.newArrayList();
		comps.add("g  " + MicroTickLoggerManager.tr("at"));
		comps.add("y  " + this.stage);
		if (this.stageDetail != null)
		{
			comps.add("y ." + this.stageDetail);
		}
		BaseText tickStageExtraText = this.stageExtra != null ? Messenger.c(this.stageExtra.toText(), "w \n"): Messenger.s("");
		return Util.getFancyText(
				null,
				Messenger.c(comps.toArray(new Object[0])),
				Messenger.c(
						tickStageExtraText,
						String.format("w %s: ", MicroTickLoggerManager.tr("Dimension")),
						Util.getDimensionNameText(this.dimensionType)
				),
				null);
	}

	private BaseText getStackTraceText()
	{
		return Messenger.c(
				"f  $",
				"^w " + Joiner.on("\n").join(this.stackTrace)
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
