package carpettisaddition.commands.lifetime.utils;

import carpet.utils.Messenger;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class LifeTimeStatistic extends TranslatableBase
{
	public static final String COLOR_MIN_TIME = "q ";
	public static final String COLOR_MAX_TIME = "c ";
	public static final String COLOR_AVG_TIME = "p ";

	public StatisticElement minTimeElement;
	public StatisticElement maxTimeElement;
	public long count;
	public long timeSum;

	public LifeTimeStatistic()
	{
		super(LifeTimeTracker.getInstance().getTranslator());
		this.clear();
	}

	public void clear()
	{
		this.count = 0;
		this.timeSum = 0;
		this.minTimeElement = new StatisticElement(Integer.MAX_VALUE, null, null, null);
		this.maxTimeElement = new StatisticElement(Integer.MIN_VALUE, null, null, null);
	}

	public boolean isValid()
	{
		return this.count > 0;
	}

	public void update(Entity entity)
	{
		long time = ((IEntity)entity).getLifeTime();
		this.count++;
		this.timeSum += time;
		StatisticElement element = new StatisticElement(time, entity.dimension, ((IEntity)entity).getSpawningPosition(), ((IEntity)entity).getRemovalPosition());
		if (time < this.minTimeElement.time)
		{
			this.minTimeElement = element;
		}
		if (time > this.maxTimeElement.time)
		{
			this.maxTimeElement = element;
		}
	}

	/**
	 * - Minimum life time: xx gt
	 * - Maximum life time: yy gt
	 * - Average life time: zz gt
	 *
	 * @param indentString spaces for indent
	 */
	public BaseText getResult(String indentString, boolean hoverMode)
	{
		BaseText indent = Messenger.s(indentString, "g");
		BaseText newLine = Messenger.s("\n");
		if (!this.isValid())
		{
			return Messenger.c(indent, "g   N/A");
		}
		indent = Messenger.c(indent, "g - ");
		return Messenger.c(
				indent,
				this.minTimeElement.getTimeWithPos(this.tr("Minimum life time"), COLOR_MIN_TIME, hoverMode),
				newLine,
				indent,
				this.maxTimeElement.getTimeWithPos(this.tr("Maximum life time"), COLOR_MAX_TIME, hoverMode),
				newLine,
				indent,
				"w " + this.tr("Average life time"),
				"g : ",
				COLOR_AVG_TIME + String.format("%.4f", (double)this.timeSum / this.count),
				"g  gt"
		);
	}

	/**
	 * @param divider a {@link Messenger#c} format divider
	 */
	public BaseText getCompressedResult(Object divider)
	{
		if (!this.isValid())
		{
			return Messenger.s("N/A", "g");
		}
		return Messenger.c(
				COLOR_MIN_TIME + this.minTimeElement.time,
				divider,
				COLOR_MAX_TIME + this.maxTimeElement.time,
				divider,
				COLOR_AVG_TIME + String.format("%.2f", (double)this.timeSum / this.count),
				"g  (gt)"
		);
	}

	private class StatisticElement
	{
		private final long time;
		private final DimensionType dimensionType;
		private final Vec3d spawningPos;
		private final Vec3d removalPos;

		private StatisticElement(long time, DimensionType dimensionType, Vec3d spawningPos, Vec3d removalPos)
		{
			this.time = time;
			this.dimensionType = dimensionType;
			this.spawningPos = spawningPos;
			this.removalPos = removalPos;
		}

		/**
		 * [hint]: 123 gt
		 * [hint]: 123 gt [S] [R]
		 */
		private BaseText getTimeWithPos(String hint, String fmt, boolean hoverMode)
		{
			BaseText text = Messenger.c(
					"w " + hint,
					"g : ",
					fmt + this.time,
					"g  gt"
			);
			if (!hoverMode)
			{
				text.append(Messenger.c(
						"w  ",
						TextUtil.getFancyText(
								null,
								Messenger.s("[S]", "e"),
								Messenger.c(
										"w " + tr("Spawning Position"),
										"g : ",
										"w " + TextUtil.getCoordinateString(this.spawningPos)
								),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.spawningPos, this.dimensionType))
						),
						"w  ",
						TextUtil.getFancyText(
								null,
								Messenger.s("[R]", "r"),
								Messenger.c(
										"w " + tr("Removal Position"),
										"g : ",
										"w " + TextUtil.getCoordinateString(this.removalPos)
								),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.removalPos, this.dimensionType))
						)
				));
			}
			return text;
		}
	}
}
