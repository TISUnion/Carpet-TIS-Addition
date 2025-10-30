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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.network.chat.BaseComponent;

public class ArmorModifyReason extends ModifyReason
{
	private final float armor;
	private final float toughness;

	public ArmorModifyReason(float armor, float toughness)
	{
		super("armor");
		this.armor = armor;
		this.toughness = toughness;
	}

	public ArmorModifyReason(LivingEntity entity)
	{
		this((float)entity.getArmorValue(), (float)entity.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
	}

	@Override
	public BaseComponent toText()
	{
		return Messenger.fancy(
				null,
				Messenger.c(
						super.toText(),
						String.format("w  %.1f + %.1f", this.armor, this.toughness)
				),
				Messenger.c(
						Messenger.attribute(SharedMonsterAttributes.ARMOR),
						"w : " + String.format("%.1f", this.armor),
						"w \n",
						Messenger.attribute(SharedMonsterAttributes.ARMOR_TOUGHNESS),
						"w : " + String.format("%.1f", this.toughness)
				),
				null
		);
	}
}
