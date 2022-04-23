package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.utils.Messenger;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.MutableText;

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
	public MutableText toText()
	{
		return Messenger.c(
				tr("death"),
				"g  (",
				Messenger.fancy(
						null,
						Messenger.s(this.damageSourceName),
						tr("death.damage_source"),
						null
				),
				"g )"
		);
	}
}
