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
import carpet.settings.Validator;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractValidator<T> extends Validator<T>
{
	private static final Translator translator = new Translator("validator");

	protected static BaseText tr(String key, Object... args)
	{
		return translator.tr(key, args);
	}

	/**
	 * Carpet interface
	 */
	@Deprecated
	@Override
	public final T validate(ServerCommandSource source, ParsedRule<T> currentRule, T inputValue, String string)
	{
		ValidationContext<T> ctx = new ValidationContext<>(source, currentRule, inputValue, string);
		T newValue = this.validate(ctx);
		if (newValue != null)
		{
			this.onRuleSet(ctx, newValue);
			if (ctx.oldValue != newValue)
			{
				this.onRuleChanged(ctx, newValue);
			}
		}
		else
		{
			this.onValidationFailure(ctx);
		}
		return newValue;
	}

	/**
	 * Carpet interface
	 */
	@Deprecated
	@Override
	public final String description()
	{
		return null;
	}

	/**
	 * When validation succeeded, it's about to be set to a value to the rule. The new value might equal to the old value
	 * @param newValue The new value to be set. Use this as a replacement of accessing the rule field, since the rule field is not updated yet
	 */
	public void onRuleSet(ValidationContext<T> ctx, T newValue)
	{
	}

	/**
	 * When validation succeeded, it's about to be set to a value to the rule, and the value is changed
	 * @param newValue The new value to be set. Use this as a replacement of accessing the rule field, since the rule field is not updated yet
	 */
	public void onRuleChanged(ValidationContext<T> ctx, T newValue)
	{
	}

	/**
	 * When validation failed
	 * <p>
	 * The default implementation sent a validation failure report to the command source, if the command source exists
	 * Remember to invoke a super call if you override this method, unless you don't want the error message to be sent
	 * <p>
	 * Messages will be like:
	 *   Wrong value for rule myRule: myValue
	 *   Some extra error message provided by the validator
	 */
	public void onValidationFailure(ValidationContext<T> ctx)
	{
		if (ctx.source == null)
		{
			return;
		}

		Messenger.tell(
				ctx.source,
				Messenger.formatting(tr("basic.failure", ctx.ruleName(), ctx.inputValue), "r"),
				true
		);

		BaseText errorMessage = this.errorMessage(ctx);
		if (errorMessage != null)
		{
			Messenger.tell(ctx.source, Messenger.formatting(errorMessage, "r"), false);
		}
	}

	/**
	 * Validate implementation
	 */
	@Nullable
	protected abstract T validate(ValidationContext<T> ctx);

	/**
	 * Provide an extra validation error message as a detailed description when validate fails
	 * If null is returned, no extra line of the error message will be sent
	 */
	public BaseText errorMessage(ValidationContext<T> ctx)
	{
		return null;
	}
}
