package carpettisaddition.commands.lifetime.removal;

import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

// for item entity and xp orb entity
public class MobPickupRemovalReason extends MobRelatedRemovalReason
{
	public MobPickupRemovalReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public BaseText toText()
	{
		return tr("mob_pickup", this.entityType.getName());
	}
}
