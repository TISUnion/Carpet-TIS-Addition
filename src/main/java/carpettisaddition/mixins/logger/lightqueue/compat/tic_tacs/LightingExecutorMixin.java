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

package carpettisaddition.mixins.logger.lightqueue.compat.tic_tacs;

import carpettisaddition.logging.loggers.lightqueue.IServerLightingProvider;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tic-TACS mod implements its own lighting executor
 * So in order to make the lightQueue logger be able to successfully record lighting tasks being executed,
 * here comes the Mixin
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"),
		@Condition(ModIds.tic_tacs)
})
@Pseudo
@Mixin(
		targets = {
				"net.gegy1000.tictacs.async.worker.LightingExecutor",  // tic-tacs-0.1.2
				"net.gegy1000.tictacs.lighting.LightingExecutor"  // at least it's here when i code this, on 2021/02/25
		},
		remap = false
)
public abstract class LightingExecutorMixin
{
	@Shadow(remap = false)
	private LightingProvider lightingProvider;

	@Dynamic
	@Inject(
			method = "processQueue",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Runnable;run()V",
					shift = At.Shift.AFTER,
					remap = false
			),
			remap = false
	)
	private void onLightingTaskExecuted(CallbackInfo ci)
	{
		if (this.lightingProvider instanceof IServerLightingProvider)
		{
			((IServerLightingProvider)this.lightingProvider).onExecutedLightUpdates();
		}
	}
}