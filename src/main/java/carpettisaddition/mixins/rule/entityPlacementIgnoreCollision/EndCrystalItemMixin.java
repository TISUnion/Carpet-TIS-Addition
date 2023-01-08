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

package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.GameUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EndCrystalItem.class)
public abstract class EndCrystalItemMixin
{
	@ModifyArg(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;isAir(Lnet/minecraft/util/math/BlockPos;)Z"
			)
	)
	private BlockPos entityPlacementIgnoreCollision_skipAirCheck(BlockPos pos)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			pos = GameUtil.getInvalidBlockPos();
		}
		return pos;
	}

	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE_ASSIGN",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					//#else
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private List<Entity> entityPlacementIgnoreCollision_skipEntityCheck(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			entities.clear();
		}
		return entities;
	}
}
