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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ArmorStandItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 1.15- mixin is handled in the version-specified class
 */
@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	/**
	 * Make the 1st segment of the if statement below always returns true
	 * if (world.isSpaceEmpty(...) && world.getOtherEntities(...).isEmpty())
	 *                ^ modifying this
	 */
	@ModifyExpressionValue(
			method = "useOn",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"
					//#else
					target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Z"
					//#endif
			)
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition1True(boolean notCollided)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			notCollided = true;
		}
		return notCollided;
	}

	/**
	 * Make the 2nd segment of the if statement below always returns true
	 * if (world.isSpaceEmpty(...) && world.getOtherEntities(...).isEmpty())
	 *                                                               ^ modifying this
	 */
	@ModifyExpressionValue(
			method = "useOn",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					remap = false
			)
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition2True(boolean isEmpty)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			isEmpty = true;
		}
		return isEmpty;
	}
}
