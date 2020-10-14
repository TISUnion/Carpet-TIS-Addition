package carpettisaddition.logging.loggers.microtick.message;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
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

	public final DimensionType dimensionType;
	public final BlockPos pos;
	public final DyeColor color;
	public final String stage, stageDetail;
	public final ToTextAble stageExtra;
	public final StackTraceElement[] stackTrace;
	public final BaseEvent event;
	public final MessageType messageType;

	public MicroTickMessage(MicroTickLogger logger, DimensionType dimensionType, BlockPos pos, DyeColor color, BaseEvent event)
	{
		this.dimensionType = dimensionType;
		this.pos = pos.toImmutable();
		this.color = color;
		this.event = event;
		this.messageType = MessageType.fromEventType(event.getEventType());
		this.stage = logger.getTickStage();
		this.stageDetail = logger.getTickStageDetail();
		this.stageExtra = logger.getTickStageExtra();
		this.stackTrace = StackTraceDeobfuscator.deobfuscateStackTrace((new Exception(logger.getClass().getName())).getStackTrace());
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
		return this.dimensionType.hashCode() ^ (this.pos.hashCode() << 4) ^ (this.color.hashCode() << 8) ^ (this.stage.hashCode() << 12);
	}

	private BaseText getHashTag()
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

	private BaseText getStage()
	{
		List<Object> comps = Lists.newArrayList();
		comps.add("g at ");
		comps.add("y " + this.stage);
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
						"w World: ",
						Util.getDimensionNameText(this.dimensionType)
				),
				null);
	}

	private BaseText getStackTrace()
	{
		return Messenger.c(
				"f $",
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
		line.add(this.getHashTag());
		line.add(event.toText());
		if (this.event.getEventType() != EventType.ACTION_END)
		{
			if (showStage)
			{
				line.add("w  ");
				line.add(this.getStage());
			}
		}
		line.add("w  ");
		line.add(this.getStackTrace());
		return Messenger.c(line.toArray(new Object[0]));
	}

	@Override
	public String toString()
	{
		return "MicroTickMessage{" +
				"dimensionType=" + dimensionType +
				", pos=" + pos +
				", color=" + color +
				", stage='" + stage + '\'' +
				", stageDetail='" + stageDetail + '\'' +
				", stageExtra=" + stageExtra +
				", event=" + event +
				", messageType=" + messageType +
				'}';
	}
}
