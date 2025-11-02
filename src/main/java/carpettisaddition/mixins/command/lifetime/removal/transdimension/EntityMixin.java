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

package carpettisaddition.mixins.command.lifetime.removal.transdimension;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.TransDimensionRemovalReason;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12100
//$$ import com.llamalad7.mixinextras.sugar.Local;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.server.level.ServerLevel;
//#else
import net.minecraft.world.level.dimension.DimensionType;
//#endif

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Inject(
			//#if MC >= 12102
			//$$ method = "teleportCrossDimension",
			//#elseif MC >= 12100
			//$$ method = "teleportTo",
			//#else
			method = "changeDimension",
			//#endif

			//#if MC < 11600
			// useful if @At is targeting the this.removed field
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11500
							target = "Lnet/minecraft/server/level/ServerLevel;addFromAnotherDimension(Lnet/minecraft/world/entity/Entity;)V"
							//#else
							//$$ target = "Lnet/minecraft/server/level/ServerLevel;addFromAnotherDimension(Lnet/minecraft/world/entity/Entity;)V"
							//#endif
					)
			),
			//#endif

			at = @At(
					//#if MC >= 11600
					//$$ value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/world/entity/Entity;removeAfterChangingDimensions()V"
					//#else
					//$$ target = "Lnet/minecraft/world/entity/Entity;removeAfterChangingDimensions()V"
					//#endif
					//#else
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/Entity;removed:Z"
					//#endif
			),
			allow = 1
	)
	private void lifetimeTracker_recordRemoval_transDimension(
			//#if 11600 <= MC && MC < 12100
			//$$ ServerLevel destination,
			//#elseif MC < 11600
			DimensionType destination,
			//#endif

			CallbackInfoReturnable<Entity> cir

			//#if MC >= 12106
			//$$ , @Local(argsOnly = true, ordinal = 1) ServerLevel destination
			//#elseif MC >= 12102
			//$$ , @Local(argsOnly = true) ServerLevel destination
			//#elseif MC >= 12100
			//$$ , @Local(ordinal = 1) ServerLevel destination
			//#endif
	)
	{
		((LifetimeTrackerTarget)this).recordRemoval(new TransDimensionRemovalReason(DimensionWrapper.of(destination)));
	}
}
