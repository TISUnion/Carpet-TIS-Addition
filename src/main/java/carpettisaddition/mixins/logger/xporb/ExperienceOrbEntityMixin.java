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

package carpettisaddition.mixins.logger.xporb;

import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbEntityMixin
{
	@Unique private boolean flagDied = false;
	@Unique private boolean flagDespawned = false;

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDI)V", at = @At("TAIL"))
	private void xpOrbLogger_onCreated(CallbackInfo ci)
	{
		XPOrbLogger.getInstance().onEntityCreated((ExperienceOrb)(Object)this);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ExperienceOrbEntity;discard()V"
					//#else
					target = "Lnet/minecraft/world/entity/ExperienceOrb;remove()V"
					//#endif
			)
	)
	private void xpOrbLogger_onDespawned(CallbackInfo ci)
	{
		if (!this.flagDespawned)
		{
			XPOrbLogger.getInstance().onEntityDespawn((ExperienceOrb)(Object)this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ExperienceOrbEntity;discard()V"
					//#else
					target = "Lnet/minecraft/world/entity/ExperienceOrb;remove()V"
					//#endif
			)
	)
	private void xpOrbLogger_onDied(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float amount)
	{
		if (!this.flagDied)
		{
			XPOrbLogger.getInstance().onEntityDied((ExperienceOrb)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
