package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpet.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class StatusEffectModifyReason extends ModifyReason
{
	private final StatusEffect statusEffect;
	private final Integer amplifier;

	public StatusEffectModifyReason(StatusEffect statusEffect, Integer amplifier)
	{
		super("StatusEffect");
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
		list.add(this.statusEffect.getName());
		if (this.amplifier != null)
		{
			list.add("w  ");
			list.add(this.amplifier <= 9 ? new TranslatableText("enchantment.level." + (this.amplifier + 1)) : Messenger.s(String.valueOf(this.amplifier)));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}
