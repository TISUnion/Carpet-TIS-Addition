package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpettisaddition.utils.Messenger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;

import java.util.Objects;

public class ArmorModifyReason extends ModifyReason
{
	private final float armor;
	private final float toughness;

	public ArmorModifyReason(float armor, float toughness)
	{
		super("Armor");
		this.armor = armor;
		this.toughness = toughness;
	}

	public ArmorModifyReason(LivingEntity entity)
	{
		this((float)entity.getArmor(), (float) Objects.requireNonNull(entity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());
	}

	@Override
	public MutableText toText()
	{
		return Messenger.fancy(
				null,
				Messenger.c(
						super.toText(),
						String.format("w  %.1f + %.1f", this.armor, this.toughness)
				),
				Messenger.c(
						Messenger.attribute(EntityAttributes.GENERIC_ARMOR),
						"w : " + String.format("%.1f", this.armor),
						"w \n",
						Messenger.attribute(EntityAttributes.GENERIC_ARMOR_TOUGHNESS),
						"w : " + String.format("%.1f", this.toughness)
				),
				null
		);
	}
}
