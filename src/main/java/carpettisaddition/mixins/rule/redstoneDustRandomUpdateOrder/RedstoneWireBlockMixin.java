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
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

//#if MC >= 12200
//$$ import net.minecraft.world.DefaultRedstoneController;
//#endif

//#if MC >= 11600
//$$ import com.google.common.collect.Lists;
//$$ import com.google.common.collect.Sets;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#else
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(
		//#if MC >= 12200
		//$$ DefaultRedstoneController.class
		//#else
		RedstoneWireBlock.class
		//#endif
)
public abstract class RedstoneWireBlockMixin
{
	private final Random random$TISCM = new Random();

	//#if MC >= 11600
	//$$ @ModifyVariable(
	//$$ 		method = "update",
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
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;clear()V"
			)
	)
	private void letsMakeTheOrderUnpredictable(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir, @Local List<BlockPos> list)
	{
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			Collections.shuffle(list, random$TISCM);
		}
	}
	//#endif
}
