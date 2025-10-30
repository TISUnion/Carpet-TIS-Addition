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

import net.minecraft.network.chat.BaseComponent;

/**
 * Validators for general usages
 */
public class Validators
{
	public static class NegativeNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() < 0;}
		@Override public BaseComponent errorMessage(ValidationContext<T> ctx) {return tr("negative_number.message");}
	}

	public static class PositiveNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() > 0;}
		@Override public BaseComponent errorMessage(ValidationContext<T> ctx) {return tr("positive_number.message");}
	}

	public static class NonNegativeNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() >= 0;}
		@Override public BaseComponent errorMessage(ValidationContext<T> ctx) {return tr("non_negative_number.message");}
	}

	public static class NonPositiveNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() <= 0;}
		@Override public BaseComponent errorMessage(ValidationContext<T> ctx) {return tr("positive_number.message");}
	}

	public static class Probability extends RangedNumberValidator<Double>
	{
		public Probability()
		{
			super(0.0D, 1.0D);
		}
	}
}
