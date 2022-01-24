package carpettisaddition.commands.lifetime.removal;

import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

public class MobConversionRemovalReason extends MobRelatedRemovalReason
{
	public MobConversionRemovalReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public BaseText toText()
	{
		return tr("mob_conversion", this.entityType.getName());
	}
}
