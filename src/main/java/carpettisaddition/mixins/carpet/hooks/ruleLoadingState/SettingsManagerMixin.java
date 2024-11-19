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

package carpettisaddition.mixins.carpet.hooks.ruleLoadingState;

import carpet.settings.SettingsManager;
import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsManager.class)
public abstract class SettingsManagerMixin
{
	@Inject(method = "loadConfigurationFromConf", at = @At("HEAD"), remap = false)
	private void loadConfigurationFromConf_startHook(CallbackInfo ci)
	{
		CarpetTISAdditionSettings.isLoadingRulesFromConfig = true;
	}

	@Inject(method = "loadConfigurationFromConf", at = @At("RETURN"), remap = false)
	private void loadConfigurationFromConf_endHook(CallbackInfo ci)
	{
		CarpetTISAdditionSettings.isLoadingRulesFromConfig = false;
	}
}
