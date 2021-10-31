package carpettisaddition.commands.lifetime.spawning;

import carpet.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.text.BaseText;

import java.util.Objects;

// for item or other entity intentionally throw by entity
public class MobThrowSpawningReason extends SpawningReason {
	private final EntityType<?> providerType;

	public MobThrowSpawningReason(EntityType<?> providerType) {
		this.providerType = providerType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MobThrowSpawningReason that = (MobThrowSpawningReason) o;
		return Objects.equals(providerType, that.providerType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(providerType);
	}

	@Override
	public BaseText toText() {
		return Messenger.c(
				"w " + this.tr("mob_throw.pre", "Thrown by "),
				this.providerType.getName(),
				"w " + this.tr("mob_throw.post", "")
		);
	}
}
