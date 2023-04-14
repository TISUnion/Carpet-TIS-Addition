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

package carpettisaddition.helpers.carpet.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced.EntityPlayerActionPackActionAccessor;

public class PlayerActionPackHelper
{
	public static EntityPlayerActionPack.Action after(int delay)
	{
		return EntityPlayerActionPackActionAccessor.invokeConstructor(
				1, 1, delay
				//#if MC >= 11600
				//$$ , false
				//#endif
		);
	}
}
