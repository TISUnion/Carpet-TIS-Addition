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
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

/**
 * 1.16+ mixin is handled in the version-specified class
 */
@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	/**
	 * Make the 1st segment of the if statement below always returns true
	 * if (itemPlacementContext.canPlace() && world.getBlockState(blockPos2).canReplace(itemPlacementContext))
	 *           ^ modifying this
	 */
	@ModifyExpressionValue(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemPlacementContext;canPlace()Z"
			)
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition1True(boolean canPlace)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			canPlace = true;
		}
		return canPlace;
	}

	/**
	 * Make the 2nd segment of the if statement below always returns true
	 * if (itemPlacementContext.canPlace() && world.getBlockState(blockPos2).canReplace(itemPlacementContext))
	 *                                                                         ^ modifying this
	 */
	@ModifyExpressionValue(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;canReplace(Lnet/minecraft/item/ItemPlacementContext;)Z"
			)
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition2True(boolean canReplace)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			canReplace = true;
		}
		return canReplace;
	}

	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private List<Entity> entityPlacementIgnoreCollision_skipEntityCheck(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			entities = Lists.newArrayList();
		}
		return entities;
	}

	private static final ThreadLocal<ItemPlacementContext> currentContext$TISCM = ThreadLocal.withInitial(() -> null);

	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemPlacementContext;canPlace()Z"
			)
	)
	private ItemPlacementContext entityPlacementIgnoreCollision_storeItemPlacementContext(ItemPlacementContext itemPlacementContext)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			currentContext$TISCM.set(itemPlacementContext);
		}
		return itemPlacementContext;
	}

	@ModifyArg(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
			),
			require = 2
	)
	private BlockPos entityPlacementIgnoreCollision_dontRemoveBlockIfNotReplaceable(BlockPos pos)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			ItemPlacementContext ctx = currentContext$TISCM.get();
			World world = ctx.getWorld();
			if (!world.getBlockState(pos).canReplace(ctx))
			{
				pos = GameUtil.getInvalidBlockPos();
			}
		}
		return pos;
	}
}
