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

import carpet.settings.ParsedRule;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ValidationContext<T>
{
	@Nullable
	public final CommandSourceStack source;
	public final ParsedRule<T> rule;
	public final T oldValue;
	public final T inputValue;
	public final String valueString;

	public ValidationContext(@Nullable CommandSourceStack source, ParsedRule<T> rule, T inputValue, String valueString)
	{
		this.source = source;
		this.rule = rule;
		this.oldValue = rule.get();
		this.inputValue = inputValue;
		this.valueString = valueString;
	}

	public T ok()
	{
		return this.inputValue;
	}

	public T failed()
	{
		return null;
	}

	public String ruleName()
	{
		return
				//#if MC >= 11901
				//$$ this.rule.name();
				//#else
				this.rule.name;
		//#endif
	}

	public Optional<CommandSourceStack> optionalSource()
	{
		return Optional.ofNullable(this.source);
	}
}
