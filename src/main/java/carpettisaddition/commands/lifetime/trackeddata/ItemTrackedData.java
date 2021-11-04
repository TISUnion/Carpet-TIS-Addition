package carpettisaddition.commands.lifetime.trackeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.BaseText;

public class ItemTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		return entity instanceof ItemEntity ? ((ItemEntity)entity).getStack().getCount() : 0L;
	}

	@Override
	protected BaseText getCountDisplayText()
	{
		return tr("item_count");
	}
}
