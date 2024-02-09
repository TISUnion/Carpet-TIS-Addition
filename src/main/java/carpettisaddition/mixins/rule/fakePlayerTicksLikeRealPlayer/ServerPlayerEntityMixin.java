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

import carpet.fakes.ServerPlayerEntityInterface;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer.FakePlayerTicker;
import carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer.PlayerActionPackCanceller;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * See carpet's mixin
 * - ( /  , 1.18) {@link carpet.mixins.ServerPlayerEntityMixin}
 * - [1.18, 1.19) {@link carpet.mixins.ServerPlayerMixin}
 * - [1.19,  /  ) {@link carpet.mixins.ServerPlayer_actionPackMixin}
 *
 * The @Inject there has priority == 1000 (default value)
 */
public abstract class ServerPlayerEntityMixin
{
	@Mixin(value = ServerPlayerEntity.class, priority = 100)
	public static abstract class Before
	{
		@Inject(method = "tick", at = @At("HEAD"))
		private void fakePlayerTicksLikeRealPlayer_cancelCarpetActionPackTicking_before(CallbackInfo ci)
		{
			if (CarpetTISAdditionSettings.fakePlayerTicksLikeRealPlayer)
			{
				ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
				if (self instanceof ServerPlayerEntityInterface)
				{
					PlayerActionPackCanceller.cancelled.set(true);
					FakePlayerTicker.getInstance().addActionPackTick(self, ((ServerPlayerEntityInterface)self).getActionPack());
				}
			}
		}
	}

	@Mixin(value = ServerPlayerEntity.class, priority = 10000)
	public static abstract class After
	{
		@Inject(method = "tick", at = @At("HEAD"))
		private void fakePlayerTicksLikeRealPlayer_cancelCarpetActionPackTicking_after(CallbackInfo ci)
		{
			PlayerActionPackCanceller.cancelled.set(false);
		}
	}
}
