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

import carpettisaddition.commands.common.counter.DyeCounter;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;

import java.util.Collections;
import java.util.Comparator;

public class XpCounter extends DyeCounter<XpCounterKey>
{
	public XpCounter(DyeColor color, Translator translator)
	{
		super(color, translator, XpCounterCommand.PREFIX);
	}

	@Override
	protected Comparator<Object2LongMap.Entry<XpCounterKey>> getReportOrderComparator()
	{
		return Collections.reverseOrder(Comparator.comparingLong(e -> e.getKey().getXpAmount()));
	}

	@Override
	protected BaseText getSymbolText()
	{
		return Messenger.s("X", Formatting.GRAY);
	}
}
