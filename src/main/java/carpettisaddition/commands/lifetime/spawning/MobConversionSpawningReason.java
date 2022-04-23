package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;

public class MobConversionSpawningReason extends MobRelatedSpawningReason
{
	public MobConversionSpawningReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public MutableText toText()
	{
		return tr("mob_conversion", this.entityType.getName());
	}
}
