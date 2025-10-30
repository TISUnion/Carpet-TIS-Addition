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

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.common.counter.DyeCounterCommand;
import net.minecraft.world.item.DyeColor;

public class XpCounterCommand extends DyeCounterCommand<XpCounterKey, XpCounter>
{
	private static final XpCounterCommand INSTANCE = new XpCounterCommand();
	private static final String NAME = "xp_counter";
	public static final String PREFIX = "xcounter";

	public XpCounterCommand()
	{
		super(NAME, PREFIX);
	}

	public static XpCounterCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected XpCounter createCounterForColor(DyeColor color)
	{
		return new XpCounter(color, this.getTranslator());
	}

	@Override
	public Object getRuleValue()
	{
		return CarpetTISAdditionSettings.hopperXpCounters;
	}

	@Override
	public boolean isActivated()
	{
		return CarpetTISAdditionSettings.hopperXpCounters;
	}

	public void record(DyeColor color, int xpAmount, int count)
	{
		this.addFor(color, new XpCounterKey(xpAmount), (long)xpAmount * count);
	}
}
