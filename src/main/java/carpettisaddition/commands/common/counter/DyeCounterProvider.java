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

package carpettisaddition.commands.common.counter;

import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DyeCounterProvider<Key extends DyeCounterKey, Counter extends DyeCounter<Key>>
{
	@NotNull
	Counter getCounter(@NotNull DyeColor color);

	@Nullable
	default Counter getCounter(String color)
	{
		DyeColor dyeColor;
		try
		{
			dyeColor = DyeColor.valueOf(color.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
		return this.getCounter(dyeColor);
	}
}
