package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;

public class MobDropSpawningReason extends MobRelatedSpawningReason
{
	public MobDropSpawningReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public MutableText toText()
	{
		return tr("mob_drop", this.entityType.getName());
	}
}
