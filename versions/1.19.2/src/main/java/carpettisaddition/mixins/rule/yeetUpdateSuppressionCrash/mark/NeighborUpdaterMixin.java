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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark;

import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin
{
	/**
	 * ModifyVariable applies too late, so we need to use ModifyArg.
	 * We still modify the local var, in case other mod wants to use it
	 */
	@SuppressWarnings("ConstantConditions")
	@ModifyArg(
			method = "tryNeighborUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
			)
	)
	private static Throwable yeetUpdateSuppressionCrash_wrapStackOverflow(
			Throwable throwable,
			@Local(ordinal = 0, argsOnly = true) World world,
			@Local(ordinal = 0, argsOnly = true) BlockPos neighborPos,
			@Local LocalRef<Throwable> ref
	)
	{
		throwable = UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, world, neighborPos);
		ref.set(throwable);
		return throwable;
	}
}
