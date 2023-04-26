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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(conflict = {
		@Condition(value = ModIds.carpet, versionPredicates = ">=1.4.49 <=1.4.76"),
		@Condition(value = ModIds.carpet_extra, versionPredicates = ">=1.4.14 <=1.4.43"),
})
@Mixin(World.class)
public abstract class WorldMixin
{
	@SuppressWarnings("ConstantConditions")
	@ModifyVariable(
			method = "updateNeighbor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
			)
	)
	private Throwable yeetUpdateSuppressionCrash_wrapStackOverflow(Throwable throwable, BlockPos sourcePos, Block sourceBlock, BlockPos neighborPos)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			if (throwable instanceof UpdateSuppressionException)
			{
				throw (UpdateSuppressionException)throwable;
			}
			if (throwable instanceof StackOverflowError || throwable instanceof OutOfMemoryError)
			{
				throw new UpdateSuppressionException(throwable, (World)(Object)this, neighborPos);
			}
		}
		return throwable;
	}
}
