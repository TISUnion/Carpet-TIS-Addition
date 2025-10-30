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

package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = {
		@Condition(ModIds.malilib),
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.15")
})
@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin
{
	// for mc < 1.14
	// the related target codes are ported so we just need to apply modification there
	// see carpettisaddition.helpers.rule.largeBarrel.DoubleBlockProperties.getBlockEntity

	@ModifyExpressionValue(
			method = "getBlockEntity",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/BlockGetter;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"
			)
	)
	private BlockEntity letsHaveAnOffThreadAccess(BlockEntity blockEntity, BlockGetter blockView, BlockPos pos)
	{
		if (CarpetTISAdditionSettings.largeBarrel && LargeBarrelHelper.enabledOffThreadBlockEntityAccess.get())
		{
			if (blockView instanceof Level)
			{
				Level world = (Level)blockView;
				blockEntity = world.getChunk(pos).getBlockEntity(pos);
			}
		}
		return blockEntity;
	}
}
