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

package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.utils.Messenger;
import net.minecraft.util.Formatting;

/**
 * A rule value listener for those feature rules using TISCM protocol
 * It will show a warning message if the feature rule is on but the rule tiscmNetworkProtocol is off
 */
public class TISCMProtocolRuleListener extends RuleChangeListener<Boolean>
{
	@Override
	public void onRuleSet(ValidationContext<Boolean> ctx, Boolean newValue)
	{
		if (ctx.source != null && newValue && !ctx.ruleName().equals("tiscmNetworkProtocol"))
		{
			if (!CarpetTISAdditionSettings.tiscmNetworkProtocol)
			{
				Messenger.tell(ctx.source, Messenger.formatting(
						tr("tiscm_protocol.protocol_not_enabled", ctx.ruleName()),
						Formatting.GRAY, Formatting.ITALIC
				));
			}
		}
	}
}
