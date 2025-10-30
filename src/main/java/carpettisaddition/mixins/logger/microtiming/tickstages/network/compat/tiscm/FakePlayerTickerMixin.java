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

package carpettisaddition.mixins.logger.microtiming.tickstages.network.compat.tiscm;

import carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer.FakePlayerTicker;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerEntitySubStage;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FakePlayerTicker.class)
public abstract class FakePlayerTickerMixin
{
	@ModifyVariable(
			method = "networkPhaseTick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lnet/minecraft/server/level/ServerPlayer;doTick()V"
					//#else
					//$$ target = "Lnet/minecraft/server/network/ServerPlayerEntity;method_14226()V"
					//#endif
			)
	)
	private ServerPlayer tiscmFakePlayerTicker_startStageDetailTickPlayer(ServerPlayer player)
	{
		MicroTimingLoggerManager.setSubTickStage(new PlayerEntitySubStage(player));
		return player;
	}

	@Inject(method = "networkPhaseTick", at = @At("RETURN"), remap = false)
	private void tiscmFakePlayerTicker_endStageDetailTickPlayer(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage(null);
	}
}
