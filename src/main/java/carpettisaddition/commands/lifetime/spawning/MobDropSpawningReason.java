package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

public class MobDropSpawningReason extends MobRelatedSpawningReason
{
	public MobDropSpawningReason(EntityType<?> providerType)
	{
		super(providerType);
	}

	@Override
	public BaseText toText()
	{
		return this.advTr("mob_drop", "Dropped by %1$s", this.providerType.getName());
	}
}
