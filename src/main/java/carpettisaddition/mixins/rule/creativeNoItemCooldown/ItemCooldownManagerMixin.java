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

package carpettisaddition.mixins.rule.creativeNoItemCooldown;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.creativeNoItemCooldown.ItemCooldownManagerWithPlayer;
import carpettisaddition.utils.EntityUtil;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCooldownManager.class)
public abstract class ItemCooldownManagerMixin implements ItemCooldownManagerWithPlayer
{
	private PlayerEntity player$TISCM = null;

	@Override
	public void setPlayer$TISCM(PlayerEntity player$TISCM)
	{
		this.player$TISCM = player$TISCM;
	}

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void creativeNoItemCooldown_preventSettingCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.creativeNoItemCooldown)
		{
			if (EntityUtil.isCreativePlayer(this.player$TISCM))
			{
				ci.cancel();
			}
		}
	}
}
