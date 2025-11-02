/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.logger.portalCreation;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.portalCreation.PortalCreationLogger;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin
{
	@Inject(
			method = "getExitPortal",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/portal/PortalForcer;createPortal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$Axis;)Ljava/util/Optional;"
			)
	)
	private void portalCreationLogger_recordEntityPre(CallbackInfoReturnable<?> cir, @Local(argsOnly = true) Entity entity, @Share("needReset") LocalBooleanRef needReset)
	{
		if (TISAdditionLoggerRegistry.__portalCreation)
		{
			needReset.set(true);
			PortalCreationLogger.entityThatCreatesThePortal.set(entity);
		}
	}

	@Inject(
			method = "getExitPortal",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/portal/PortalForcer;createPortal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$Axis;)Ljava/util/Optional;",
					shift = At.Shift.AFTER
			)
	)
	private void portalCreationLogger_recordEntityPost(CallbackInfoReturnable<?> cir, @Share("needReset") LocalBooleanRef needReset)
	{
		if (needReset.get())
		{
			PortalCreationLogger.entityThatCreatesThePortal.remove();
		}
	}
}
