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

package carpettisaddition.mixins.command.lifetime.spawning.spawner;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.15- mixin is handled in the version-specified class
 */
@Mixin(BaseSpawner.class)
public abstract class MobSpawnerLogicMixin
{
	private Entity spawnedEntity$lifeTimeTracker;

	@ModifyArg(
			//#if MC >= 11700
			//$$ method = "serverTick",
			//#else
			method = "tick",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;tryAddFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;tryAddFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)Z"
					//#endif
			),
			index = 0
	)
	private Entity recordSpawnedEntityLifeTimeTracker(Entity entity)
	{
		this.spawnedEntity$lifeTimeTracker = entity;
		return entity;
	}

	@Inject(
			//#if MC >= 11700
			//$$ method = "serverTick",
			//#else
			method = "tick",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#else
					target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V"
					//#endif
			)
	)
	private void onSpawnerLogicSpawnEntityLifeTimeTracker(CallbackInfo ci)
	{
		if (this.spawnedEntity$lifeTimeTracker != null)
		{
			((LifetimeTrackerTarget)this.spawnedEntity$lifeTimeTracker).recordSpawning(LiteralSpawningReason.SPAWNER);
		}
	}
}
