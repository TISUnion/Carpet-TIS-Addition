package carpettisaddition.commands.lifetime.removal;

import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;

// for item entity and xp orb entity
public class MobPickupRemovalReason extends MobRelatedRemovalReason
{
	public MobPickupRemovalReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public MutableText toText()
	{
		return tr("mob_pickup", this.entityType.getName());
	}
}
