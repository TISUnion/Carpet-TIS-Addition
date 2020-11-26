package carpettisaddition.commands.lifetime.spawning;

import carpet.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

import java.util.Objects;

// for item entity and xp orb entity
public class MobDropSpawningReason extends SpawningReason
{
	private final EntityType<?> providerType;

	public MobDropSpawningReason(EntityType<?> providerType)
	{
		this.providerType = providerType;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MobDropSpawningReason that = (MobDropSpawningReason) o;
		return Objects.equals(providerType, that.providerType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(providerType);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				"w " + this.tr("mob_drop.pre", "Dropped by "),
				this.providerType.getName(),
				"w " + this.tr("mob_drop.post", "")
		);
	}
}
