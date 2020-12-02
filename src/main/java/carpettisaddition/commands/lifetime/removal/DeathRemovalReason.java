package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class DeathRemovalReason extends RemovalReason
{
	private final DamageSource damageSource;

	public DeathRemovalReason(DamageSource damageSource)
	{
		this.damageSource = Objects.requireNonNull(damageSource);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeathRemovalReason)) return false;
		DeathRemovalReason that = (DeathRemovalReason) o;
		return Objects.equals(this.damageSource, that.damageSource);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.damageSource);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				"w " + this.tr("Death"),
				"g  (",
				TextUtil.getFancyText(
						null,
						Messenger.s(this.damageSource.getName()),
						Messenger.s(this.tr("Damage source")),
						null
				),
				"g )"
		);
	}
}
