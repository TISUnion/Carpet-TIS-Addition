package carpettisaddition.commands.lifetime.removal;

import net.minecraft.entity.EntityType;

import java.util.Objects;

public abstract class MobRelatedRemovalReason extends RemovalReason
{
	protected final EntityType<?> entityType;

	public MobRelatedRemovalReason(EntityType<?> entityType)
	{
		this.entityType = entityType;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MobRelatedRemovalReason that = (MobRelatedRemovalReason) o;
		return Objects.equals(entityType, that.entityType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(entityType);
	}
}
