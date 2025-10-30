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

package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ITntEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * priority 2000 to make sure it injects after carpet's modifyTNTAngle injection
 */
@Mixin(value = PrimedTnt.class, priority = 2000)
public abstract class TntEntityMixin extends Entity implements ITntEntity
{
	private Vec3 initializedVelocity;
	private Vec3 initializedPosition;

	public TntEntityMixin(EntityType<?> type, Level world)
	{
		super(type, world);
	}

	@Inject(
			method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/entity/LivingEntity;)V",
			at = @At("TAIL")
	)
	private void recordInitializedAngle(CallbackInfo ci)
	{
		this.initializedVelocity = this.getDeltaMovement();
		this.initializedPosition = this.position();
	}

	@Override
	public boolean dataRecorded()
	{
		return this.getInitializedVelocity() != null && this.getInitializedPosition() != null;
	}

	@Override
	public Vec3 getInitializedVelocity()
	{
		return this.initializedVelocity;
	}

	@Override
	public Vec3 getInitializedPosition()
	{
		return this.initializedPosition;
	}
}
