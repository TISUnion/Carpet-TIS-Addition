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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12105
//$$ import net.minecraft.world.entity.LivingEntity;
//#endif

@Mixin(BucketItem.class)
public abstract class BucketItemMixin
{
	@ModifyExpressionValue(
			//#if MC >= 1.17.0
			//$$ method = "emptyContents",
			//#else
			method = "emptyBucket",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12111
					//$$ target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;",
					//$$ ordinal = 0
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/world/level/dimension/DimensionType;ultraWarm()Z"
					//#else
					target = "Lnet/minecraft/world/level/dimension/Dimension;isUltraWarm()Z"
					//#endif
			)
	)
	//#if MC >= 12111
	//$$ private Object creativeNetherWaterPlacement_impl(Object /* Boolean */ doesWaterVaporize,
	//#else
	private boolean creativeNetherWaterPlacement_impl(boolean doesWaterVaporize,
	//#endif
			//#if MC >= 12105
			//$$ @Local(argsOnly = true) @Nullable LivingEntity entity
			//#else
			@Local(argsOnly = true) @Nullable Player player
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.creativeNetherWaterPlacement)
		{
			//#if MC >= 12105
			//$$ if (entity instanceof Player player && player.isCreative())
			//#else
			if (player != null && player.isCreative())
			//#endif
			{
				doesWaterVaporize = false;
			}
		}
		return doesWaterVaporize;
	}
}
