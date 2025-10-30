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

package carpettisaddition.helpers.carpet.loggerRestriction;

import net.minecraft.network.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;

public class RestrictionCheckResult
{
	private final boolean passed;
	@Nullable
	private final BaseComponent errorMessage;

	private RestrictionCheckResult(boolean passed, @Nullable BaseComponent errorMessage)
	{
		this.passed = passed;
		this.errorMessage = errorMessage;
	}

	public static RestrictionCheckResult ok()
	{
		return new RestrictionCheckResult(true, null);
	}

	public static RestrictionCheckResult failed(BaseComponent errorMessage)
	{
		return new RestrictionCheckResult(false, errorMessage);
	}

	public static RestrictionCheckResult bool(boolean condition, BaseComponent errorMessage)
	{
		return new RestrictionCheckResult(condition, errorMessage);
	}

	public boolean isPassed()
	{
		return this.passed;
	}

	// not null iff. isPassed() == true
	@Nullable
	public BaseComponent getErrorMessage()
	{
		return this.errorMessage;
	}
}
