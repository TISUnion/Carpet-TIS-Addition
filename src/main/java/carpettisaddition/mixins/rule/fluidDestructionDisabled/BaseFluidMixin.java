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

package carpettisaddition.mixins.rule.fluidDestructionDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
import net.minecraft.fluid.FlowableFluid;
//#else
//$$ import net.minecraft.fluid.BaseFluid;
//#endif

@Mixin(
		//#if MC >= 11600
		FlowableFluid.class
		//#else
		//$$ BaseFluid.class
		//#endif
)
public class BaseFluidMixin
{
	@Inject(
			method = "flow",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					target = "Lnet/minecraft/fluid/FlowableFluid;beforeBreakingBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
					//#else
					//$$ target = "Lnet/minecraft/fluid/BaseFluid;beforeBreakingBlock(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
					//#endif
			),
			cancellable = true
	)
	private void stopBreakingBlock(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.fluidDestructionDisabled)
		{
			ci.cancel();
		}
	}
}
