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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VaultSharedData.class)
public abstract class VaultSharedDataMixin
{
	/**
	 * By hooking here instead of {@link net.minecraft.block.vault.VaultServerData#getRewardedPlayers},
	 * mods who invokes getRewardedPlayers can still get the unmodified result
	 */
	@ModifyExpressionValue(
			method = "lambda$updateConnectedPlayersWithinRange$4",  // lambda method in updateConnectedPlayers
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z",
					remap = false
			),
			remap = true
	)
	private static boolean vaultBlacklistDisabled_everyoneCanConnectToAndActivateTheVault(boolean contains)
	{
		if (CarpetTISAdditionSettings.vaultBlacklistDisabled)
		{
			contains = false;
		}
		return contains;
	}
}
