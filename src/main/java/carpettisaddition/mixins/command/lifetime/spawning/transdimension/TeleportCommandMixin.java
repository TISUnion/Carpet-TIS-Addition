/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.lifetime.spawning.transdimension;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.TransDimensionRemovalReason;
import carpettisaddition.commands.lifetime.spawning.TransDimensionSpawningReason;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *           mc >= 1.21.2: subproject 1.21.3
 * 1.19.4 <= mc <  1.21.2: subproject 1.19.4
 *           mc <  1.19.4: subproject 1.15.2 (main project)  <--------
 *
 * The command creates and transfers the new entity inside {@link TeleportCommand}.
 */
@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin
{
	@Inject(
			method = "performTeleport",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.17
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;addDuringTeleport(Lnet/minecraft/world/entity/Entity;)V",
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;addFromAnotherDimension(Lnet/minecraft/world/entity/Entity;)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private static void lifetimeTracker_recordCommandTeleport(
			CallbackInfo ci,
			@Local(argsOnly = true) ServerLevel destination,
			@Local(ordinal = 0) Entity newEntity,
			@Local(ordinal = 1) Entity oldEntity
	)
	{
		((LifetimeTrackerTarget)oldEntity).recordRemoval$TISCM(new TransDimensionRemovalReason(DimensionWrapper.of(destination)));
		((LifetimeTrackerTarget)newEntity).recordSpawning$TISCM(new TransDimensionSpawningReason(DimensionWrapper.of(oldEntity)));
	}
}
