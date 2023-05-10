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
import carpettisaddition.utils.mixin.testers.YeetUpdateSuppressionCrashTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(require = @Condition(type = Condition.Type.TESTER, tester = YeetUpdateSuppressionCrashTester.class))
@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin
{
	@SuppressWarnings("ConstantConditions")
	@ModifyVariable(
			method = "tryNeighborUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
			)
	)
	private static Throwable yeetUpdateSuppressionCrash_wrapStackOverflow(Throwable throwable, World world, BlockState state, BlockPos neighborPos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			if (throwable instanceof UpdateSuppressionException)
			{
				throw (UpdateSuppressionException)throwable;
			}
			if (throwable instanceof StackOverflowError || throwable instanceof OutOfMemoryError)
			{
				throw new UpdateSuppressionException(throwable, world, neighborPos);
			}
		}
		return throwable;
	}
}
