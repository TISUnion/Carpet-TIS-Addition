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

package carpettisaddition.commands.scounter;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.common.counter.DyeCounterCommand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;

public class SupplierCounterCommand extends DyeCounterCommand<SupplierCounterKey, SupplierCounter>
{
	private static final SupplierCounterCommand INSTANCE = new SupplierCounterCommand();
	private static final String NAME = "supplier_counter";
	public static final String PREFIX = "scounter";

	public SupplierCounterCommand()
	{
		super(NAME, PREFIX);
	}

	public static SupplierCounterCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected SupplierCounter createCounterForColor(DyeColor color)
	{
		return new SupplierCounter(color, this.getTranslator());
	}

	@Override
	public Object getRuleValue()
	{
		return CarpetTISAdditionSettings.hopperNoItemCost;
	}

	@Override
	public boolean isActivated()
	{
		return CarpetTISAdditionSettings.hopperNoItemCost;
	}

	public void record(DyeColor color, ItemStack previousStack, ItemStack currentStack)
	{
		int delta = currentStack.getCount() - previousStack.getCount();
		// previous -> current == more -> less, so delta < 0
		if (delta < 0)
		{
			this.addFor(color, new SupplierCounterKey(previousStack.getItem()), -delta);
		}
	}
}
