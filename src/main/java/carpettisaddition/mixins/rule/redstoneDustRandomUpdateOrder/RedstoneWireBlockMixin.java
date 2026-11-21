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

package carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder;

import carpettisaddition.helpers.rule.redstoneDustRandomUpdateOrder.RedstoneDustUpdateOrderRandomizer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

//#if MC >= 12102
//$$ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
//#endif

//#if MC >= 11600
//$$ import com.google.common.collect.Lists;
//$$ import com.google.common.collect.Sets;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import java.util.Set;
//#else
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(
		//#if MC >= 12102
		//$$ DefaultRedstoneWireEvaluator.class
		//#else
		RedStoneWireBlock.class
		//#endif
)
public abstract class RedstoneWireBlockMixin
{
	//#if MC >= 11600
	//$$ @ModifyVariable(
	//$$ 		method = "updatePowerStrength",
	//$$ 		ordinal = 0,
	//$$ 		at = @At(
	//$$ 				value = "LOAD",
	//$$ 				ordinal = 2
	//$$ 		)
	//$$ )
	//$$ private Set<BlockPos> letsMakeTheOrderUnpredictable(Set<BlockPos> set, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos pos)
	//$$ {
	//$$ 	if (RedstoneDustUpdateOrderRandomizer.isRuleEnabled())
	//$$ 	{
	//$$ 		List<BlockPos> list = Lists.newArrayList(set);
	//$$ 		RedstoneDustUpdateOrderRandomizer.shuffle(list, pos, world);
	//$$ 		set = Sets.newLinkedHashSet(list);
	//$$ 	}
	//$$ 	return set;
	//$$ }
	//#else
	@Inject(
			method = "updatePowerStrength",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;clear()V"
			)
	)
	private void letsMakeTheOrderUnpredictable(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir, @Local List<BlockPos> list)
	{
		if (RedstoneDustUpdateOrderRandomizer.isRuleEnabled())
		{
			RedstoneDustUpdateOrderRandomizer.shuffle(list, pos, world);
		}
	}
	//#endif
}
