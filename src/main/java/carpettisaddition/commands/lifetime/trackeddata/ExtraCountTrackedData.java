package carpettisaddition.commands.lifetime.trackeddata;

import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

import java.util.Map;

public abstract class ExtraCountTrackedData extends BasicTrackedData
{
	public final Map<SpawningReason, Long> spawningExtraCountMap = Maps.newHashMap();
	public final Map<RemovalReason, Long> removalExtraCountMap = Maps.newHashMap();

	protected abstract long getExtraCount(Entity entity);

	@Override
	public void updateSpawning(Entity entity, SpawningReason reason)
	{
		super.updateSpawning(entity, reason);
		this.spawningExtraCountMap.put(reason, this.spawningExtraCountMap.getOrDefault(reason, 0L) + this.getExtraCount(entity));
	}

	@Override
	public void updateRemoval(Entity entity, RemovalReason reason)
	{
		super.updateRemoval(entity, reason);
		this.removalExtraCountMap.put(reason, this.removalExtraCountMap.getOrDefault(reason, 0L) + this.getExtraCount(entity));
	}

	protected abstract BaseText getCountDisplayText();

	private BaseText attachExtraCountHoverText(BaseText text, long extraCount, long ticks)
	{
		return Messenger.hover(text, Messenger.c(
				this.getCountDisplayText(),
				"g : ",
				CounterUtil.ratePerHourText(extraCount, ticks, "wgg")
		));
	}

	@Override
	public BaseText getSpawningCountText(long ticks)
	{
		return this.attachExtraCountHoverText(super.getSpawningCountText(ticks), getLongMapSum(this.spawningExtraCountMap), ticks);
	}

	@Override
	public BaseText getRemovalCountText(long ticks)
	{
		return this.attachExtraCountHoverText(super.getRemovalCountText(ticks), getLongMapSum(this.removalExtraCountMap), ticks);
	}

	@Override
	protected BaseText getSpawningReasonWithRate(SpawningReason reason, long ticks, long count, long total)
	{
		return this.attachExtraCountHoverText(super.getSpawningReasonWithRate(reason, ticks, count, total), this.spawningExtraCountMap.getOrDefault(reason, 0L), ticks);
	}

	@Override
	protected BaseText getRemovalReasonWithRate(RemovalReason reason, long ticks, long count, long total)
	{
		return this.attachExtraCountHoverText(super.getRemovalReasonWithRate(reason, ticks, count, total), this.removalExtraCountMap.getOrDefault(reason, 0L), ticks);
	}
}
