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

package carpettisaddition.helpers.rule.fakePlayerNameExtra;

import carpet.settings.ParsedRule;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.settings.validator.AbstractCheckerValidator;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.network.chat.BaseComponent;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FakePlayerNameExtraValidator extends AbstractCheckerValidator<String>
{
	private final Map<ParsedRule<?>, String> lastDangerousInput = Maps.newHashMap();

	@Override
	protected boolean validateContext(ValidationContext<String> ctx)
	{
		if (ctx.source == null || CarpetTISAdditionSettings.isLoadingRulesFromConfig)
		{
			// non-human input, accept anyway
			return true;
		}

		if (!ctx.inputValue.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra) && !Pattern.matches("[a-zA-Z_0-9]{1,16}", ctx.inputValue))
		{
			Consumer<BaseComponent> messenger = msg -> Messenger.tell(ctx.source, Messenger.s(msg.getString(), "r"));
			messenger.accept(tr("fake_player_name_extra.warn.found", ctx.inputValue, ctx.ruleName()));
			if (!Objects.equals(this.lastDangerousInput.get(ctx.rule), ctx.inputValue))
			{
				messenger.accept(tr("fake_player_name_extra.warn.blocked"));
				this.lastDangerousInput.put(ctx.rule, ctx.inputValue);
				return false;
			}
			messenger.accept(tr("fake_player_name_extra.warn.applied"));
		}
		this.lastDangerousInput.remove(ctx.rule);
		return true;
	}

	@Override
	public BaseComponent errorMessage(ValidationContext<String> ctx)
	{
		return tr("fake_player_name_extra.message");
	}
}
