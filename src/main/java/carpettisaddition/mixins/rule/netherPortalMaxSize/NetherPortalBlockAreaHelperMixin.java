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

package carpettisaddition.mixins.rule.netherPortalMaxSize;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11600
//$$ import net.minecraft.world.dimension.AreaHelper;
//#else
import net.minecraft.world.level.block.NetherPortalBlock;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ AreaHelper.class
		//#else
		NetherPortalBlock.PortalShape.class
		//#endif
)
public abstract class NetherPortalBlockAreaHelperMixin
{
	@ModifyExpressionValue(
			method = {
					//#if MC >= 11600
					//$$ //#if MC >= 12103
					//$$ //$$ "getValidatedWidth",
					//$$ //$$ "getWidth",
					//$$ //#else
					//$$ "method_30495",
					//$$ "method_30493",
					//$$ //#endif
					//$$ "method_30492",
					//$$ "method_30496",
					//$$ "method_30490",
					//#else
					"<init>",
					"calculatePortalHeight",
					//#endif
			},
			at = @At(
					value = "CONSTANT",
					args = "intValue=21"
			)
	)
	private
	//#if MC >= 12103
	//$$ static
	//#endif
	int netherPortalMaxSize_modifyUpperLimit1(int limit)
	{
		if (CarpetTISAdditionSettings.netherPortalMaxSize != CarpetTISAdditionSettings.VANILLA_NETHER_PORTAL_MAX_SIZE)
		{
			limit = CarpetTISAdditionSettings.netherPortalMaxSize;
		}
		return limit;
	}

	@ModifyExpressionValue(
			method = "isValid",
			at = @At(
					value = "CONSTANT",
					args = "intValue=21"
			)
	)
	private int netherPortalMaxSize_modifyUpperLimit2(int limit)
	{
		if (CarpetTISAdditionSettings.netherPortalMaxSize != CarpetTISAdditionSettings.VANILLA_NETHER_PORTAL_MAX_SIZE)
		{
			limit = CarpetTISAdditionSettings.netherPortalMaxSize;
		}
		return limit;
	}

	//#if MC < 11600
	@ModifyExpressionValue(
			method = "getDistanceUntilEdge",
			at = @At(value = "CONSTANT", args = "intValue=22")
	)
	private int netherPortalMaxSize_modifyLowerLimit(int limit)
	{
		if (CarpetTISAdditionSettings.netherPortalMaxSize != CarpetTISAdditionSettings.VANILLA_NETHER_PORTAL_MAX_SIZE)
		{
			limit = CarpetTISAdditionSettings.netherPortalMaxSize + 1;
		}
		return limit;
	}
	//#endif
}
