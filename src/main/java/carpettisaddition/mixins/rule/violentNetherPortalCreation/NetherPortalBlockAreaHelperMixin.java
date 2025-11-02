/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.violentNetherPortalCreation;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.BlockUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
//$$ import net.minecraft.world.level.portal.PortalShape;
//#else
import net.minecraft.world.level.block.NetherPortalBlock;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ PortalShape.class
		//#else
		NetherPortalBlock.PortalShape.class
		//#endif
)
public abstract class NetherPortalBlockAreaHelperMixin
{
	@Shadow private int numPortalBlocks;

	@ModifyReturnValue(method = "isEmpty", at = @At("TAIL"))
	private
	//#if MC >= 11600
	//$$ static
	//#endif
	boolean violentNetherPortalCreation_impl(boolean canReplace, @Local(argsOnly = true) BlockState state)
	{
		if (!canReplace)
		{
			switch (CarpetTISAdditionSettings.violentNetherPortalCreation)
			{
				case ALL:
					canReplace = state.getBlock() != Blocks.OBSIDIAN;
					break;
				case REPLACEABLE:
					canReplace = BlockUtils.isReplaceable(state);
					break;
			}
		}
		return canReplace;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void violentNetherPortalCreation_youFoundZeroPortalBlocks(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.violentNetherPortalCreation == CarpetTISAdditionSettings.ViolentNetherPortalCreationOptions.ALL)
		{
			this.numPortalBlocks = 0;
		}
	}
}
