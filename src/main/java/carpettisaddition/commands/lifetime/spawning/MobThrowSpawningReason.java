package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

public class MobThrowSpawningReason extends MobRelatedSpawningReason
{
	public MobThrowSpawningReason(EntityType<?> providerType)
	{
		super(providerType);
	}

	@Override
	public BaseText toText()
	{
		return this.advTr("mob_throw", "Thrown by %1$s", this.providerType.getName());
	}
}
