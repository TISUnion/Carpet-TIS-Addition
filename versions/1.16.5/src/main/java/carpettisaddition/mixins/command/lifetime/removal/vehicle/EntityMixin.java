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

package carpettisaddition.mixins.command.lifetime.removal.vehicle;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public abstract boolean hasVehicle();

	@Inject(
			//#if MC >= 1.21.9
			//$$ method = "startRiding(Lnet/minecraft/entity/Entity;ZZ)Z",
			//#else
			method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z",
			//#endif
			at = @At("RETURN")
	)
	private void lifetimeTracker_recordRemoval_vehicleMounting(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.hasVehicle())
		{
			((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.VEHICLE_MOUNTING);
		}
	}
}
