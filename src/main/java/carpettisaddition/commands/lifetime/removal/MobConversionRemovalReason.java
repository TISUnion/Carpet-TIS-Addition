package carpettisaddition.commands.lifetime.removal;

import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;

public class MobConversionRemovalReason extends MobRelatedRemovalReason
{
	public MobConversionRemovalReason(EntityType<?> entityType)
	{
		super(entityType);
	}

	@Override
	public MutableText toText()
	{
		return tr("mob_conversion", this.entityType.getName());
	}
}
