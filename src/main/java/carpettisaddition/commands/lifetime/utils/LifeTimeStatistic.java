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
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

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
		this.minTimeElement = new StatisticElement(Integer.MAX_VALUE, null, null);
		this.maxTimeElement = new StatisticElement(Integer.MIN_VALUE, null, null);
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
		StatisticElement element = new StatisticElement(time, entity.getEntityWorld().getRegistryKey(), ((IEntity)entity).getSpawnPosition());
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
	public BaseText getResult(String indentString)
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
				this.minTimeElement.getTimeWithPos(this.tr("Minimum life time"), COLOR_MIN_TIME),
				newLine,
				indent,
				this.maxTimeElement.getTimeWithPos(this.tr("Maximum life time"), COLOR_MAX_TIME),
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
		private final RegistryKey<World> dimensionType;
		private final Vec3d spawnPos;

		private StatisticElement(long time, RegistryKey<World> dimensionType, Vec3d spawnPos)
		{
			this.time = time;
			this.dimensionType = dimensionType;
			this.spawnPos = spawnPos;
		}

		private BaseText getTimeWithPos(String hint, String fmt)
		{
			return TextUtil.getFancyText(
					null,
					Messenger.c(
							"w " + hint,
							"g : ",
							fmt + this.time,
							"g  gt"
					),
					Messenger.c(
							"w " + tr("Spawn Position"),
							"g : ",
							"w " + TextUtil.getCoordinateString(this.spawnPos)
					),
					new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.spawnPos, this.dimensionType))
			);
		}
	}
}
