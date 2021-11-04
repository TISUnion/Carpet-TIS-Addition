package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;

public class EnchantmentModifyReason extends ModifyReason
{
	private final int point;

	public EnchantmentModifyReason(int point)
	{
		super("Enchantment");
		this.point = point;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(super.toText(), String.format("w  (EPF=%d)", this.point));
	}
}
