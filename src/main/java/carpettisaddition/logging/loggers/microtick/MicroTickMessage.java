package carpettisaddition.logging.loggers.microtick;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;
import java.util.List;

public class MicroTickMessage
{
	public final DimensionType dimensionType;
	public final BlockPos pos;
	public final DyeColor color;
	public final String stage, stageDetail;
	public final TickStage stageExtra;
	public final StackTraceElement[] stackTrace;
	public final Object [] texts;

	MicroTickMessage(MicroTickLogger logger, DimensionType dimensionType, BlockPos pos, DyeColor color, Object[] texts)
	{
		this.dimensionType = dimensionType;
		this.pos = pos.toImmutable();
		this.color = color;
		this.texts = texts;
		this.stage = logger.getTickStage();
		this.stageDetail = logger.getTickStageDetail();
		this.stageExtra = logger.getTickStageExtra();
		this.stackTrace = (new Exception(logger.getClass().getName())).getStackTrace();
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
		boolean ret = this.dimensionType == o.dimensionType && this.pos.equals(o.pos) && this.color.equals(o.color) && this.stage.equals(o.stage);
		ret |= this.texts.length == o.texts.length;
		if (!ret)
		{
			return ret;
		}
		for (int i = 0; i < this.texts.length; i++)
			if (this.texts[i] instanceof String && !this.texts[i].equals(o.texts[i]))
				return false;
		return ret;
	}

	public int hashCode()
	{
		return this.dimensionType.hashCode() + this.pos.hashCode() * 2 + this.color.hashCode();
	}

	Text getHashTag()
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

	Text getStage()
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

	Text getStackTrace()
	{
		return Messenger.c(
				"f $",
				"^w " + Arrays.toString(this.stackTrace)
		);
	}
}
