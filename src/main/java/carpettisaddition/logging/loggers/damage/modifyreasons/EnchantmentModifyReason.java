package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.utils.TextUtil;
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
		return Messenger.c(
				"w " + DamageLogger.getTranslator().tr("enchantments"),
				TextUtil.getSpaceText(),
				String.format("w (EPF=%d)", this.point)
		);
	}
}
