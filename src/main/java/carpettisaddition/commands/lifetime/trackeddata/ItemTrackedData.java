package carpettisaddition.commands.lifetime.trackeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.MutableText;

public class ItemTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		return entity instanceof ItemEntity ? ((ItemEntity)entity).getStack().getCount() : 0L;
	}

	@Override
	protected MutableText getCountDisplayText()
	{
		return tr("item_count");
	}
}
