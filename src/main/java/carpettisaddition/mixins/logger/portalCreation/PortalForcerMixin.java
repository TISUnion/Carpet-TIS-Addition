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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

//#if MC > 1.16.0
//$$ import net.minecraft.util.math.Vec3i;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#else
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
//#endif

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin
{
	@Shadow
	@Final
	private ServerWorld world;

	@Inject(
			//#if 1.16.0 <= MC && MC < 1.17.0
			//$$ method = "method_30482",
			//#else
			method = "createPortal",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"
			)
	)
	private void portalCreationLogger_recordIsFloatingPlatform(CallbackInfoReturnable<Boolean> cir, @Share("isFloatingPlatform") LocalBooleanRef isFloatingPlatform)
	{
		isFloatingPlatform.set(true);
	}

	//#if MC >= 1.16.0
	//$$ @ModifyArg(
	//#else
	// Trick to capture portal x,y,z / blockPos in mc < 1.16: use the parameters of the last `mutable.set`'s first call
	@ModifyArgs(
	//#endif
			//#if 1.16.0 <= MC && MC < 1.17.0
			//$$ method = "method_30482",
			//#else
			method = "createPortal",
			//#endif
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/block/Blocks;NETHER_PORTAL:Lnet/minecraft/block/Block;"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.16.0
					//$$ target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(Lnet/minecraft/util/math/Vec3i;III)Lnet/minecraft/util/math/BlockPos$Mutable;",
					//#else
					target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(III)Lnet/minecraft/util/math/BlockPos$Mutable;",
					//#endif
					ordinal = 0
			)
	)
	private
	//#if MC >= 1.16.0
	//$$ Vec3i
	//#else
	void
	//#endif
	portalCreationLogger_doLog(
			//#if MC >= 1.16.0
			//$$ Vec3i arg0,
			//#else
			Args args,
			@Local(argsOnly = true) Entity entity,
			//#endif
			@Share("isNotFirstSet") LocalBooleanRef isNotFirstSet,
			@Share("isFloatingPlatform") LocalBooleanRef isFloatingPlatform
	)
	{
		if (!TISAdditionLoggerRegistry.__portalCreation || isNotFirstSet.get())
		{
			//#if MC >= 1.16.0
			//$$ return arg0;
			//#else
			return;
			//#endif
		}
		isNotFirstSet.set(true);

		//#if MC < 1.16.0
		PortalCreationLogger.entityThatCreatesThePortal.set(entity);
		try
		//#endif
		{
			//#if MC >= 1.16.0
			//$$ BlockPos portalPos = new BlockPos(arg0);
			//#else
			BlockPos portalPos = new BlockPos(args.get(0), args.get(1), args.get(2));
			//#endif
			PortalCreationLogger.getInstance().onNetherPortalCreation(this.world, portalPos, isFloatingPlatform.get());
		}
		//#if MC < 1.16.0
		finally
		{
			PortalCreationLogger.entityThatCreatesThePortal.remove();
		}
		//#endif

		//#if MC >= 1.16.0
		//$$ return arg0;
		//#endif
	}
}
