package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.entity.EntityType;

import java.util.Objects;

public abstract class MobRelatedSpawningReason extends SpawningReason
{
	protected final EntityType<?> providerType;

	public MobRelatedSpawningReason(EntityType<?> providerType)
	{
		this.providerType = providerType;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MobRelatedSpawningReason that = (MobRelatedSpawningReason) o;
		return Objects.equals(providerType, that.providerType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(providerType);
	}
}
