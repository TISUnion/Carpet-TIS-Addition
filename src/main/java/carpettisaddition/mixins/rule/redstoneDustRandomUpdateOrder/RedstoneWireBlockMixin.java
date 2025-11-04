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

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

//#if MC >= 12102
//$$ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
//#endif

//#if MC >= 11600
//$$ import com.google.common.collect.Lists;
//$$ import com.google.common.collect.Sets;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#else
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
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
	@Unique
	private final Random random$TISCM = new Random();

	//#if MC >= 11600
	//$$ @ModifyVariable(
	//$$ 		method = "updatePowerStrength",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
	//$$ 		)
	//$$ )
	//$$ private Set<BlockPos> letsMakeTheOrderUnpredictable(Set<BlockPos> set)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
	//$$ 	{
	//$$ 		List<BlockPos> list = Lists.newArrayList(set);
	//$$ 		Collections.shuffle(list, this.random$TISCM);
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
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			Collections.shuffle(list, random$TISCM);
		}
	}
	//#endif
}
