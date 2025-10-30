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

package carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UpdateSuppressionYeeter
{
	public static void noop()
	{
		// load the classes in advance
		// to prevent NoClassDefFoundError due to stack overflow again when loading this class
		UpdateSuppressionContext.noop();
		UpdateSuppressionExceptions.noop();
	}

	@NotNull
	public static Throwable tryReplaceWithWrapper(@NotNull Throwable throwable, @Nullable Level world, BlockPos pos)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			// no UpdateSuppressionException, try to wrap it
			if (!extractInCauses(throwable).isPresent())
			{
				Throwable wrapped = (Throwable)UpdateSuppressionExceptions.createWrapper(throwable, world, pos);
				if (wrapped != null)
				{
					throwable = wrapped;
				}
			}
		}
		return throwable;
	}

	public static Optional<UpdateSuppressionException> extractInCauses(Throwable throwable)
	{
		for (; throwable != null; throwable = throwable.getCause())
		{
			if (throwable instanceof UpdateSuppressionException)
			{
				return Optional.of((UpdateSuppressionException)throwable);
			}
		}
		return Optional.empty();
	}
}
