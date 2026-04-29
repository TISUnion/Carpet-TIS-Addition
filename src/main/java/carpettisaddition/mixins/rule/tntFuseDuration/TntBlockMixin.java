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

package carpettisaddition.mixins.rule.tntFuseDuration;

import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin
{
	@ModifyArg(
			method = "wasExploded",
			at = @At(
					value = "INVOKE",
					//#if MC >= 26.2
					//$$ target = "Lnet/minecraft/world/entity/item/PrimedTnt;getRandomShortFuse(ILnet/minecraft/util/RandomSource;)I"
					//#elseif MC >= 11900
					//$$ target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
					//#else
					target = "Ljava/util/Random;nextInt(I)I",
					remap = false
					//#endif
			),
			index = 0
	)
	private int tntFuseDuration_makeSureItIsNotTooSmall(int value)
	{
		//#if MC >= 26.2
		//$$ // see `net.minecraft.world.entity.item.PrimedTnt#getRandomShortFuse`, where a `random.nextInt(fuse / 4)` will be called
		//$$ return Math.max(value, 4);
		//#else
		return Math.max(value, 1);
		//#endif
	}
}
