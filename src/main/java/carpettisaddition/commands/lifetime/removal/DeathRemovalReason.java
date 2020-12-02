package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class DeathRemovalReason extends RemovalReason
{
	private final String damageSourceName;

	public DeathRemovalReason(DamageSource damageSource)
	{
		this.damageSourceName = damageSource.getName();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeathRemovalReason)) return false;
		DeathRemovalReason that = (DeathRemovalReason) o;
		return Objects.equals(this.damageSourceName, that.damageSourceName);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.damageSourceName);
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				"w " + this.tr("Death"),
				"g  (",
				TextUtil.getFancyText(
						null,
						Messenger.s(this.damageSourceName),
						Messenger.s(this.tr("Damage source")),
						null
				),
				"g )"
		);
	}
}
