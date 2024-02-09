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
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerActionPackSubStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerActionSubStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerEntitySubStage;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FakePlayerTicker.class)
public abstract class FakePlayerTickerMixin
{
	@ModifyArg(
			method = "addActionPackTick",
			at = @At(
					value = "INVOKE",
					target = "Lcarpettisaddition/utils/GameUtil;submitAsyncTask(Lnet/minecraft/util/thread/ThreadExecutor;Ljava/lang/Runnable;)V"
			),
			remap = false
	)
	private Runnable tiscmFakePlayerTicker_startProcessActionPack(Runnable runnable, @Local(argsOnly = true) ServerPlayerEntity player)
	{
		return () -> {
			// reset at carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.MinecraftServerMixin
			// don't set TickStage.PLAYER_ACTION here, cuz it does not deserve it
			MicroTimingLoggerManager.setSubTickStage(new PlayerActionPackSubStage(player));
			runnable.run();
		};
	}
}
