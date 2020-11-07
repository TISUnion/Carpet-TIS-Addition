package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpet.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

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
		this((float)entity.getArmor(), (float)entity.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue());
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				TextUtil.attachColor(TextUtil.getAttributeText(EntityAttributes.ARMOR), Formatting.BLUE),
				TextUtil.getSpaceText(),
				"t " + String.format("%.1f", this.armor),
				"g , ",
				TextUtil.attachColor(TextUtil.getAttributeText(EntityAttributes.ARMOR_TOUGHNESS), Formatting.BLUE),
				TextUtil.getSpaceText(),
				"t " + String.format("%.1f", this.toughness)
		);
	}
}
