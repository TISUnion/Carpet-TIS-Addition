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

package carpettisaddition.mixins.carpet.tweaks.loggerRestriction;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
//#disable-remap
import carpet.settings.ParsedRule;
//#enable-remap
import carpet.settings.Validator;
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.carpet.loggerRestriction.CarpetLoggerRestriction;
import carpettisaddition.helpers.carpet.loggerRestriction.ruleSwitch.LoggerSwitchRuleCommon;
import carpettisaddition.helpers.carpet.loggerRestriction.ruleSwitch.LoggerSwitchValidator;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//#if MC < 11700
//$$ import com.google.common.collect.ImmutableList;
//#endif

//#if MC >= 11901
//$$ @SuppressWarnings({"deprecation", "removal"})
//#endif
//#disable-remap
@Mixin(ParsedRule.class)
//#enable-remap
public abstract class ParsedRuleMixin<T>
{
	@Shadow(remap = false) @Final public String name;
	//#if MC >= 11700
	@Shadow(remap = false) @Final public List<String> categories;
	@Shadow(remap = false) @Final @Mutable public List<String> options;
	//#else
	//$$ @Shadow(remap = false) @Final public ImmutableList<String> categories;
	//$$ @Shadow(remap = false) @Final @Mutable public ImmutableList<String> options;
	//#endif
	@Shadow(remap = false) public abstract T get();
	@Shadow(remap = false) @Final public Class<T> type;
	@Shadow(remap = false) @Final public List<Validator<T>> validators;
	@Shadow(remap = false) public boolean isStrict;

	@SuppressWarnings("unchecked")
	@Inject(
			method = "<init>",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11700
							target = "Ljava/util/List;of()Ljava/util/List;"
							//#else
							//$$ target = "Lcom/google/common/collect/ImmutableList;of()Lcom/google/common/collect/ImmutableList;"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					target = "Ljava/util/List;isEmpty()Z",
					//#else
					//$$ target = "Lcom/google/common/collect/ImmutableList;isEmpty()Z",
					//#endif
					ordinal = 0
			),
			remap = false
	)
	private void loggerRestriction_ruleTweak(CallbackInfo ci)
	{
		if (this.categories.contains(CarpetTISAdditionSettings.TIS) && this.categories.contains(CarpetTISAdditionSettings.LOGGER))
		{
			if (this.type != String.class)
			{
				CarpetTISAdditionMod.LOGGER.warn("TISCM logger switch rule {} is not a string, found type {}", this.name, this.type);
				return;
			}

			// rule name format: "loggerFooBar", indicating logger "fooBar"
			final String SUFFIX = "logger";
			if (!this.name.startsWith(SUFFIX))
			{
				CarpetTISAdditionMod.LOGGER.warn("TISCM logger switch rule {} has invalid name", this.name);
				return;
			}
			String ruleNameRemaining = this.name.substring(SUFFIX.length());
			String loggerName = !ruleNameRemaining.isEmpty() ?
					ruleNameRemaining.substring(0, 1).toLowerCase() + ruleNameRemaining.substring(1) :
					ruleNameRemaining;

			this.isStrict = false;
			this.validators.add((Validator<T>)new LoggerSwitchValidator());
			this.options = LoggerSwitchRuleCommon.OPTIONS;

			TISAdditionLoggerRegistry.addLoggerRegisteredCallback(() -> {
				Logger logger = LoggerRegistry.getLogger(loggerName);
				if (logger == null)
				{
					CarpetTISAdditionMod.LOGGER.warn("TISCM logger switch rule {} has its logger {} not found", this.name, loggerName);
					return;
				}

				CarpetLoggerRestriction.addLoggerRuleSwitch(logger, this.name, () -> (String) this.get());
				CarpetTISAdditionMod.LOGGER.debug("TISCM logger switch rule {} enabled for logger {}", this.name, loggerName);
			});
		}
	}
}
