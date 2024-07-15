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

package carpettisaddition.mixins.rule.sandDupingFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity
{
	public FallingBlockEntityMixin(EntityType<? extends FallingBlockEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					shift = At.Shift.AFTER,
					//#if MC >= 12100
					//$$ target = "Lnet/minecraft/entity/FallingBlockEntity;tickPortalTeleportation()V"
					//#else
					target = "Lnet/minecraft/entity/FallingBlockEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
					//#endif
			),
			cancellable = true
	)
	private void afterPortalTeleportation(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.sandDupingFix)
		{
			// one-line code to fix
			// thank mojang for not fixing
			if (
					//#if MC >= 11700
					//$$ this.isRemoved()
					//#else
					this.removed
					//#endif
			)
			{
				ci.cancel();
			}
		}
	}
}
