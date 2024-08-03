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

package carpettisaddition.mixins.rule.vaultBlacklistDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultServerData.class)
public abstract class VaultServerDataMixin
{
	@Inject(method = "markPlayerAsRewarded", at = @At("HEAD"), cancellable = true)
	private void vaultBlacklistDisabled_dontAddPlayerToTheBlackList(PlayerEntity player, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.vaultBlacklistDisabled)
		{
			ci.cancel();
		}
	}

	@Inject(method = "hasRewardedPlayer", at = @At("HEAD"), cancellable = true)
	private void vaultBlacklistDisabled_noOneIsInTheBlacklist(CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.vaultBlacklistDisabled)
		{
			cir.setReturnValue(false);
		}
	}
}
