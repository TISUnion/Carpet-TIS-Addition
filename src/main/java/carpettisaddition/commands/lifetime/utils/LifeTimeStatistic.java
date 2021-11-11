package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;

public class LifeTimeStatistic extends TranslationContext
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
		long time = ((LifetimeTrackerTarget)entity).getLifeTime();
		this.count++;
		this.timeSum += time;
		StatisticElement element = new StatisticElement(time, DimensionWrapper.of(entity), ((LifetimeTrackerTarget)entity).getSpawningPosition(), ((LifetimeTrackerTarget)entity).getRemovalPosition());
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
				this.minTimeElement.getTimeWithPos(tr("minimum_life_time"), COLOR_MIN_TIME, hoverMode),
				newLine,
				indent,
				this.maxTimeElement.getTimeWithPos(tr("maximum_life_time"), COLOR_MAX_TIME, hoverMode),
				newLine,
				indent,
				tr("average_life_time"),
				"g : ",
				COLOR_AVG_TIME + String.format("%.4f", (double)this.timeSum / this.count),
				"g  gt"
		);
	}

	public BaseText getCompressedResult(boolean showGtSuffix)
	{
		if (!this.isValid())
		{
			return Messenger.s("N/A", "g");
		}
		BaseText text = Messenger.c(
				COLOR_MIN_TIME + this.minTimeElement.time,
				"g /",
				COLOR_MAX_TIME + this.maxTimeElement.time,
				"g /",
				COLOR_AVG_TIME + String.format("%.2f", (double)this.timeSum / this.count)
		);
		if (showGtSuffix)
		{
			text.append(Messenger.c("g  (gt)"));
		}
		return text;
	}

	private class StatisticElement
	{
		private final long time;
		private final DimensionWrapper dimensionType;
		private final Vec3d spawningPos;
		private final Vec3d removalPos;

		private StatisticElement(long time, DimensionWrapper dimensionType, Vec3d spawningPos, Vec3d removalPos)
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
		private BaseText getTimeWithPos(BaseText hint, String fmt, boolean hoverMode)
		{
			BaseText text = Messenger.c(
					hint,
					"g : ",
					fmt + this.time,
					"g  gt"
			);
			if (!hoverMode)
			{
				text.append(Messenger.c(
						"w  ",
						Messenger.fancy(
								null,
								Messenger.s("[S]", "e"),
								Messenger.c(tr("spawning_position"), "g : ", "w " + TextUtil.coord(this.spawningPos)),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.spawningPos, this.dimensionType))
						),
						"w  ",
						Messenger.fancy(
								null,
								Messenger.s("[R]", "r"),
								Messenger.c(tr("removal_position"), "g : ", "w " + TextUtil.coord(this.removalPos)),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.removalPos, this.dimensionType))
						)
				));
			}
			return text;
		}
	}
}
