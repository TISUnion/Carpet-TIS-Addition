package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.BaseText;

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
	public BaseText toText()
	{
		return TextUtil.getFancyText(
				null,
				Messenger.c(
						super.toText(),
						String.format("w  %.1f + %.1f", this.armor, this.toughness)
				),
				Messenger.c(
						TextUtil.getAttributeText(EntityAttributes.GENERIC_ARMOR),
						"w : " + String.format("%.1f", this.armor),
						"w \n",
						TextUtil.getAttributeText(EntityAttributes.GENERIC_ARMOR_TOUGHNESS),
						"w : " + String.format("%.1f", this.toughness)
				),
				null
		);
	}
}
