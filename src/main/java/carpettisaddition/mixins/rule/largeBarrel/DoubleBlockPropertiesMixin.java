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

package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoubleBlockCombiner.class)
public abstract class DoubleBlockPropertiesMixin
{
	@Inject(
			method = "combineWithNeigbour",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"
			)
	)
	private static <S extends BlockEntity> void tweaksGetStateResultForLargeBarrelPre(CallbackInfoReturnable<DoubleBlockCombiner.NeighborCombineResult<S>> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.gettingLargeBarrelPropertySource.get())
			{
				LargeBarrelHelper.applyAxisOnlyDirectionTesting.set(true);
			}
		}
	}

	@Inject(
			method = "combineWithNeigbour",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
					shift = At.Shift.AFTER
			)
	)
	private static <S extends BlockEntity> void tweaksGetStateResultForLargeBarrelPost(CallbackInfoReturnable<DoubleBlockCombiner.NeighborCombineResult<S>> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.gettingLargeBarrelPropertySource.get())
			{
				LargeBarrelHelper.applyAxisOnlyDirectionTesting.set(false);
			}
		}
	}
}
