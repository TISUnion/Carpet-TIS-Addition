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

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.translations.TranslationContext;
import net.minecraft.network.chat.BaseComponent;

public class ModifyReason extends TranslationContext
{
	public static final ModifyReason HELMET = new ModifyReason("wearing_a_helmet");
	public static final ModifyReason SHIELD = new ModifyReason("holding_a_shield");
	public static final ModifyReason RECENTLY_HIT = new ModifyReason("recently_hit");
	public static final ModifyReason DIFFICULTY = new ModifyReason("difficulty");
	public static final ModifyReason IMMUNE = new ModifyReason("immuse_to_damage");
	public static final ModifyReason INVULNERABLE = new ModifyReason("invulnerable");
	public static final ModifyReason RESPAWN_PROTECTION = new ModifyReason("respawn_protection");
	public static final ModifyReason PVP_DISABLED = new ModifyReason("pvp_disabled");

	private final String translationName;

	protected ModifyReason(String translationName)
	{
		super(DamageLogger.getInstance().getTranslator());
		this.translationName = translationName;
	}

	public BaseComponent toText()
	{
		return tr("modify_reason." + this.translationName);
	}
}
