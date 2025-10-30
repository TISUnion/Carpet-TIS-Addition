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

package carpettisaddition.mixins.rule.fakePlayerTicksLikeRealPlayer;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer.FakePlayerTicker;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin
{
	@WrapOperation(
			method = "tick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lcarpet/patches/EntityPlayerMPFake;doTick()V"
					//#else
					//$$ target = "Lcarpet/patches/EntityPlayerMPFake;method_14226()V"
					//#endif
			)
	)
	private void fakePlayerTicksLikeRealPlayer_delayedPlayerEntityTick(EntityPlayerMPFake player, Operation<Void> original)
	{
		if (CarpetTISAdditionSettings.fakePlayerTicksLikeRealPlayer)
		{
			FakePlayerTicker.getInstance().addPlayerEntityTick(player);
			return;
		}

		// vanilla carpet
		original.call(player);
	}
}
