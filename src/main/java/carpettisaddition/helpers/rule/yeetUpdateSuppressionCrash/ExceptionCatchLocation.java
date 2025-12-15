/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.translations.Translator;
import net.minecraft.network.chat.BaseComponent;

public enum ExceptionCatchLocation
{
	WORLD_TICKING,
	SERVER_ASYNC_TASK,
	PLAYER_ENTITY_TICKING,
	PLAYER_REMOVAL,
	PACKET_HANDLING;

	private static final Translator translator = new Translator("rule.yeetUpdateSuppressionCrash.catch_location");

	public BaseComponent toText()
	{
		return translator.tr(this.name().toLowerCase());
	}
}
