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

package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17 <1.20.5"),
		@Condition(ModIds.lithium)
})
@Mixin(HopperHelper.class)
public abstract class HopperHelperMixin
{
	@Inject(
			method = "vanillaGetBlockInventory",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;",
					remap = true
			),
			cancellable = true,
			remap = false
	)
	private static void useLargeBarrelInventoryMaybe(
			Level world, BlockPos blockPos, CallbackInfoReturnable<Container> cir,
			@Local BlockState blockState,
			@Local Block block
	)
	{
		// note: inventory is always null
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (block instanceof BarrelBlock)
			{
				Container largeBarrel = LargeBarrelHelper.getInventory(blockState, world, blockPos);
				if (largeBarrel != null)
				{
					cir.setReturnValue(largeBarrel);
				}
			}
		}
	}
}