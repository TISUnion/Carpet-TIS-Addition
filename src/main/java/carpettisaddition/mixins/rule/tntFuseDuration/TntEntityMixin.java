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

package carpettisaddition.mixins.rule.tntFuseDuration;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.TntEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin
{
	@Shadow public abstract void setFuse(int fuse);

	@Inject(
			method = {
					"<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
					"<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V"
			},
			at = @At("TAIL")
	)
	private void modifyFuseTimer_tntFuseDuration(CallbackInfo ci)
	{
		this.setFuse(CarpetTISAdditionSettings.tntFuseDuration);
	}

	@ModifyArg(
			method = "initDataTracker",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/data/DataTracker;startTracking(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V"
			),
			index = 1
	)
	private Object getCustomFuseTimer(Object oldValue)
	{
		return CarpetTISAdditionSettings.tntFuseDuration;
	}
}
