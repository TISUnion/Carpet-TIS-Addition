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

package carpettisaddition.settings.validator;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractCheckerValidator<T> extends AbstractValidator<T>
{
	@Override
	protected final @Nullable T validate(ValidationContext<T> ctx)
	{
		boolean result;
		try
		{
			result = this.validateValue(ctx.inputValue);
		}
		catch (UnsupportedOperationException e)
		{
			result = this.validateContext(ctx);
		}
		return result ? ctx.inputValue : null;
	}

	// Implement one of the following methods

	protected boolean validateContext(ValidationContext<T> ctx)
	{
		throw new UnsupportedOperationException();
	}

	protected boolean validateValue(T value)
	{
		throw new UnsupportedOperationException();
	}
}
