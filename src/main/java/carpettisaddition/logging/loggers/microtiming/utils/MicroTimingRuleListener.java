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

package carpettisaddition.logging.loggers.microtiming.utils;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class MicroTimingRuleListener extends RuleChangeListener<Boolean>
{
	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("rule_listener");

	@Override
	public void onRuleChanged(ValidationContext<Boolean> ctx, @NotNull Boolean newValue)
	{
		if (ctx.source != null && ctx.source.getWorld() != null && newValue)
		{
			if (!MicroTimingUtil.isBlockUpdateInstant(ctx.source.getWorld()))
			{
				String ruleName = "instantBlockUpdaterReintroduced";
				BaseText ruleText = Messenger.fancy(
						Messenger.s(ruleName),
						translator.tr("click_to_see_rule"),
						new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/carpet " + ruleName)
				);
				Messenger.tell(ctx.source, Messenger.formatting(
						translator.tr("instant_block_updater_promote", ruleText),
						Formatting.GRAY, Formatting.ITALIC
				));
			}
		}
	}
}
