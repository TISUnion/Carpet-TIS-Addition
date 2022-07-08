package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.BaseText;

import java.util.List;

public class StatusEffectModifyReason extends ModifyReason
{
	private final StatusEffect statusEffect;
	private final Integer amplifier;

	public StatusEffectModifyReason(StatusEffect statusEffect, Integer amplifier)
	{
		super("Status effect");
		this.statusEffect = statusEffect;
		this.amplifier = amplifier;
	}
	public StatusEffectModifyReason(StatusEffect statusEffect)
	{
		this(statusEffect, null);
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(super.toText());
		list.add("w  ");
		list.add(
				//#if MC >= 11500
				this.statusEffect.getName()
				//#else
				//$$ Messenger.tr(this.statusEffect.getTranslationKey())
				//#endif
		);
		if (this.amplifier != null)
		{
			list.add("w  ");
			list.add(this.amplifier <= 9 ? Messenger.tr("enchantment.level." + (this.amplifier + 1)) : Messenger.s(String.valueOf(this.amplifier)));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}
