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

package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpettisaddition.helpers.carpet.tweaks.logger.projectile.ProjectileLoggerTarget;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.TrajectoryLoggerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// prevent ProjectileEntity(1.15) from being remapped into PersistentProjectileEntity(1.16)
//#if MC >= 11600
import net.minecraft.entity.projectile.ProjectileEntity;
//#else
//$$ import net.minecraft.entity.projectile.ProjectileEntity;
//#endif

// smaller priority to make this execute before carpet's logger creation
@Mixin(
		//#if MC >= 11600
		value = ProjectileEntity.class,
		//#else
		//$$ value = ProjectileEntity.class,
		//#endif
		priority = 500
)
public abstract class ProjectileEntityMixin implements ProjectileLoggerTarget
{
	@Unique
	private HitResult hitResult;

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
	private void recordEntityInfoForCarpetTrajectoryLogHelper(CallbackInfo ci)
	{
		TrajectoryLoggerUtil.currentEntity.set((Entity)(Object)this);
	}

	@Inject(
			//#if MC >= 11600
			method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V",
			//#else
			//$$ method = "onHit(Lnet/minecraft/util/hit/HitResult;)V",
			//#endif
			at = @At("HEAD")
	)
	private void recordHitPoint(HitResult hitResult, CallbackInfo ci)
	{
		this.hitResult = hitResult;
	}

	@Override
	public HitResult getHitResult()
	{
		return this.hitResult;
	}
}
