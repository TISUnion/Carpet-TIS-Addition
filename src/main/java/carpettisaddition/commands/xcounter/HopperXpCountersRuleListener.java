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

package carpettisaddition.commands.xcounter;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.mixin.testers.LithiumBlockHopperTester;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

public class HopperXpCountersRuleListener extends RuleChangeListener<Boolean>
{
	protected static BaseText tr(String key, Object... args)
	{
		return XpCounterCommand.getInstance().getTranslator().tr(key, args);
	}

	@Override
	public void onRuleChanged(ValidationContext<Boolean> ctx, Boolean newValue)
	{
		if (newValue != null && newValue && new LithiumBlockHopperTester().isLithiumRuleEnabled())
		{
			if (!CarpetTISAdditionSettings.isLoadingRulesFromConfig && ctx.source != null)
			{
				Messenger.tell(ctx.source, Messenger.formatting(
						tr("lithium_hopper_sleeping_notice"),
						Formatting.GRAY, Formatting.ITALIC
				));
			}
		}
	}
}
