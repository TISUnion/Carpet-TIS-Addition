/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.xcounter;

import carpettisaddition.commands.common.counter.DyeCounterKey;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;

import java.util.Objects;

public class XpCounterKey implements DyeCounterKey
{
	private final int xpAmount;
	private static final Translator translator = XpCounterCommand.getInstance().getTranslator();

	public XpCounterKey(int xpAmount)
	{
		this.xpAmount = xpAmount;
	}

	public int getXpAmount()
	{
		return this.xpAmount;
	}

	@Override
	public BaseText getText()
	{
		return Messenger.hover(
				Messenger.s(this.xpAmount),
				translator.tr("key_hover", this.xpAmount, this.getOrbSize())
		);
	}

	/**
	 * Reference: {@link net.minecraft.entity.ExperienceOrbEntity#getOrbSize}
	 */
	private int getOrbSize()
	{
		final int[] THRESHOLDS = {0, 3, 7, 17, 37, 73, 149, 307, 617, 1237, 2477};
		for (int i = THRESHOLDS.length - 1; i >= 0; i--)
		{
			if (this.xpAmount >= THRESHOLDS[i])
			{
				return i;
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		XpCounterKey that = (XpCounterKey)o;
		return xpAmount == that.xpAmount;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(xpAmount);
	}
}
