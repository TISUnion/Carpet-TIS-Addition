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
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// used in mc [1.16, 1.21)
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@Inject(
			//#if MC >= 1.17.0
			//$$ method = "getPortalRect",
			//#else
			method = "method_30330",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.17.0
					//$$ target = "Lnet/minecraft/world/PortalForcer;createPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"
					//#else
					target = "Lnet/minecraft/world/PortalForcer;method_30482(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"
					//#endif
			)
	)
	private void portalCreationLogger_recordEntityPre(CallbackInfoReturnable<?> callbackInfoReturnable, @Share("needReset") LocalBooleanRef needReset)
	{
		if (TISAdditionLoggerRegistry.__portalCreation)
		{
			needReset.set(true);
			PortalCreationLogger.entityThatCreatesThePortal.set((ServerPlayerEntity)(Object)this);
		}
	}

	@Inject(
			//#if MC >= 1.17.0
			//$$ method = "getPortalRect",
			//#else
			method = "method_30330",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.17.0
					//$$ target = "Lnet/minecraft/world/PortalForcer;createPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;",
					//#else
					target = "Lnet/minecraft/world/PortalForcer;method_30482(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;",
					//#endif
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
