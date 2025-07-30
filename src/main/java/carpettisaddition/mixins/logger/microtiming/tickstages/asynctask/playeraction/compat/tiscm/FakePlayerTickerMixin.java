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

package carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.playeraction.compat.tiscm;

import carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer.FakePlayerTicker;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerActionPackSubStage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FakePlayerTicker.class)
public abstract class FakePlayerTickerMixin
{
	// don't set TickStage.PLAYER_ACTION here, cuz it does not deserve it

	@ModifyVariable(
			method = "asyncTaskPhaseTick",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Runnable;run()V",
					remap = false
			),
			remap = false
	)
	private ServerPlayerEntity tiscmFakePlayerTicker_startProcessActionPack(ServerPlayerEntity player)
	{
		MicroTimingLoggerManager.setSubTickStage(new PlayerActionPackSubStage(player));
		return player;
	}

	@Inject(method = "asyncTaskPhaseTick", at = @At("RETURN"), remap = false)
	private void tiscmFakePlayerTicker_endProcessActionPack(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage(null);
	}
}
