package carpettisaddition.commands.lifetime.trackeddata;

import carpet.utils.Messenger;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.BaseText;

import java.util.Map;

public class ItemTrackedData extends TrackedData
{
	public final Map<SpawningReason, Long> spawningItemCountMap = Maps.newHashMap();
	public final Map<RemovalReason, Long> removalItemCountMap = Maps.newHashMap();

	@Override
	public void updateSpawning(Entity itemEntity, SpawningReason reason)
	{
		super.updateSpawning(itemEntity, reason);
		if (itemEntity instanceof ItemEntity)
		{
			long newCount = this.spawningItemCountMap.getOrDefault(reason, 0L) + ((ItemEntity)itemEntity).getStack().getCount();
			this.spawningItemCountMap.put(reason, newCount);
		}
	}

	@Override
	public void updateRemoval(Entity itemEntity, RemovalReason reason)
	{
		super.updateRemoval(itemEntity, reason);
		if (itemEntity instanceof ItemEntity)
		{
			long newCount = this.removalItemCountMap.getOrDefault(reason, 0L) + ((ItemEntity)itemEntity).getStack().getCount();
			this.removalItemCountMap.put(reason, newCount);
		}
	}

	private BaseText attachItemCountHoverText(BaseText text, long itemCount, long ticks)
	{
		return TextUtil.attachHoverText(text, Messenger.c(
				"q " + this.tr("Item Count"),
				"g : ",
				CounterUtil.ratePerHourText(itemCount, ticks, "wgg")
		));
	}

	@Override
	public BaseText getSpawningCountText(long ticks)
	{
		return this.attachItemCountHoverText(super.getSpawningCountText(ticks), getLongMapSum(this.spawningItemCountMap), ticks);
	}

	@Override
	public BaseText getRemovalCountText(long ticks)
	{
		return this.attachItemCountHoverText(super.getRemovalCountText(ticks), getLongMapSum(this.removalItemCountMap), ticks);
	}

	@Override
	protected BaseText getSpawningReasonWithRate(SpawningReason reason, long ticks, long count, long total)
	{
		return this.attachItemCountHoverText(super.getSpawningReasonWithRate(reason, ticks, count, total), this.spawningItemCountMap.getOrDefault(reason, 0L), ticks);
	}

	@Override
	protected BaseText getRemovalReasonWithRate(RemovalReason reason, long ticks, long count, long total)
	{
		return this.attachItemCountHoverText(super.getRemovalReasonWithRate(reason, ticks, count, total), this.removalItemCountMap.getOrDefault(reason, 0L), ticks);
	}
}
