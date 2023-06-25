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

package carpettisaddition.mixins.rule.creativeNetherWaterPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin
{
	@ModifyExpressionValue(
			method = "placeFluid",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/dimension/DimensionType;isUltrawarm()Z"
					//#else
					target = "Lnet/minecraft/world/dimension/Dimension;doesWaterVaporize()Z"
					//#endif
			),
			require = 0
	)
	private boolean creativeNetherWaterPlacement(
			boolean doesWaterVaporize,
			/* parent method parameters v */
			@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult
	)
	{
		if (CarpetTISAdditionSettings.creativeNetherWaterPlacement)
		{
			if (player != null && player.isCreative())
			{
				doesWaterVaporize = false;
			}
		}
		return doesWaterVaporize;
	}
}
