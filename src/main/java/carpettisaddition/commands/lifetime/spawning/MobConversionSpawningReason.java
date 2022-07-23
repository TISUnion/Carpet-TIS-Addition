package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

public class MobConversionSpawningReason extends MobRelatedSpawningReason
{
	public MobConversionSpawningReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public BaseText toText()
	{
		return tr("mob_conversion", Messenger.entityType(this.entityType));
	}
}
