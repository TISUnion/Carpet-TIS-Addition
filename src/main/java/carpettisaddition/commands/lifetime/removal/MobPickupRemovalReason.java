package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

import java.util.Objects;

// for item entity and xp orb entity
public class MobPickupRemovalReason extends RemovalReason
{
	private final EntityType<?> pickerType;

	public MobPickupRemovalReason(EntityType<?> pickerType)
	{
		this.pickerType = pickerType;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MobPickupRemovalReason that = (MobPickupRemovalReason) o;
		return Objects.equals(pickerType, that.pickerType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(pickerType);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				"w " + this.tr("mob_pickup.pre", "Picked up by "),
				this.pickerType.getName(),
				"w " + this.tr("mob_pickup.post", "")
		);
	}
}
