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

package carpettisaddition.mixins.command.lifetime.spawning.item;

import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.thrown.ThrownExperienceBottleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11700
//$$ import carpettisaddition.commands.lifetime.utils.LifetimeMixinUtil;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//#else
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import net.minecraft.entity.Entity;
//#endif

@Mixin(ThrownExperienceBottleEntity.class)
public abstract class ThrownExperienceBottleEntityMixin
{
	//#if MC >= 11700
	//$$ @Inject(
	//$$ 		method = "onCollision",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"
	//$$ 		)
	//$$ )
	//$$ private void onXPBottleDroppedXpLifeTimeTracker(CallbackInfo ci)
	//$$ {
	//$$ 	LifetimeMixinUtil.xpOrbSpawningReason.set(LiteralSpawningReason.ITEM);
	//$$ }
	//#else
	@ModifyArg(
			method = "onCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity onXPBottleDroppedXpLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.ITEM);
		return entity;
	}
	//#endif
}
