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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark.blockUpdateStackOverflow;

import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Level.class)
public abstract class WorldMixin
{
	// use in < mc1.19

	/**
	 * ModifyVariable applies too late, so we need to use ModifyArg.
	 * We still modify the local var, in case other mod wants to use it
	 */
	@SuppressWarnings("ConstantConditions")
	@ModifyArg(
			method = "neighborChanged",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"
			)
	)
	private Throwable yeetUpdateSuppressionCrash_wrapSoundSuppression(
			Throwable throwable,
			@Local(ordinal = 1, argsOnly = true) BlockPos neighborPos,
			@Local LocalRef<Throwable> ref
	)
	{
		if (throwable instanceof UpdateSuppressionException || throwable instanceof StackOverflowError || throwable instanceof OutOfMemoryError)
		{
			throwable = UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, (Level)(Object)this, neighborPos);
			ref.set(throwable);
		}
		return throwable;
	}
}
