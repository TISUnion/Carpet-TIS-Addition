package carpettisaddition.logging.loggers.microtick;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.enums.MessageType;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.text.BaseText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;

import static java.lang.Integer.min;

public class MicroTickMessage
{
	private static final int MAX_INDENT = 4;
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
	public final MessageType messageType;
	public final String stage, stageDetail;
	public final TickStage stageExtra;
	public final StackTraceElement[] stackTrace;
	public final Object [] texts;
	public final int indentation;

	public MicroTickMessage(MicroTickLogger logger, DimensionType dimensionType, BlockPos pos, DyeColor color, MessageType messageType, Object[] texts)
	{
		this.dimensionType = dimensionType;
		this.pos = pos.toImmutable();
		this.color = color;
		this.texts = texts;
		this.messageType = messageType;
		this.indentation = logger.getIndent();
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
		boolean ret = this.dimensionType.equals(o.dimensionType) && this.pos.equals(o.pos) && this.color.equals(o.color) && this.stage.equals(o.stage) && this.messageType.equals(o.messageType) && this.texts.length == o.texts.length;
		if (!ret)
		{
			return ret;
		}
		// this.texts.length == o.texts.length
		for (int i = 0; i < this.texts.length; i++)
			if (this.texts[i] instanceof String && !this.texts[i].equals(o.texts[i]))
				return false;
		return ret;
	}

	@Override
	public int hashCode()
	{
		return this.dimensionType.hashCode() + this.pos.hashCode() * 2 + this.color.hashCode();
	}

	private Text getHashTag()
	{
		String text = MicroTickUtil.getColorStyle(this.color) + " # ";
		Text ret;
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

	private Text getStage()
	{
		List<Object> comps = Lists.newLinkedList();
		comps.add("g at ");
		comps.add("y " + this.stage);
		if (this.stageDetail != null)
		{
			comps.add("y ." + this.stageDetail);
		}
		Text tickStageExtraText = this.stageExtra != null ? Messenger.c(this.stageExtra.toText(), "w \n"): Messenger.s("");
		Text text = Messenger.c(comps.toArray(new Object[0]));
		text.getStyle().setHoverEvent(
				new HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						Messenger.c(
								tickStageExtraText,
								"w World: ",
								Util.getDimensionNameText(this.dimensionType)
						)
				)
		);
		return text;
	}

	private Text getStackTrace()
	{
		return Messenger.c(
				"f $",
				"^w " + Joiner.on("\n").join(this.stackTrace)
		);
	}

	public BaseText toText()
	{
		List<Object> line = Lists.newLinkedList();
		line.add(this.getHashTag());
		if (this.indentation > 0)
		{
			line.add("w " + INDENTATIONS.get(min(this.indentation, MAX_INDENT)));
		}
		for (Object text: this.texts)
		{
			if (text instanceof Text || text instanceof String)
			{
				line.add(text);
			}
		}
		if (this.messageType != MessageType.ACTION_END)
		{
			line.add("w  ");
			line.add(this.getStage());
			line.add("w  ");
			line.add(this.getStackTrace());
		}
		return Messenger.c(line.toArray(new Object[0]));
	}
}
