package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

public class MobDropSpawningReason extends MobRelatedSpawningReason
{
	public MobDropSpawningReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public BaseText toText()
	{
		return tr("mob_drop", Messenger.entityType(this.entityType));
	}
}
