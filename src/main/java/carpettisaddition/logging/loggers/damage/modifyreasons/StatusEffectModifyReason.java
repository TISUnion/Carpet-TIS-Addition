/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

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
