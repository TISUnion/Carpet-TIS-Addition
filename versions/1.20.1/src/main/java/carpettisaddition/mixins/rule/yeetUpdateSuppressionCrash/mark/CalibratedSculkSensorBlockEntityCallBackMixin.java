/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.net.UnknownServiceException;

@Mixin(CalibratedSculkSensorBlockEntity.VibrationUser.class)
public abstract class CalibratedSculkSensorBlockEntityCallBackMixin
{
	@WrapOperation(
			method = "getBackSignal",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"
			)
	)
	private <T extends Comparable<T>> T yeetUpdateSuppressionCrash_wrapSoundSuppression(
			BlockState instance, Property<T> property, Operation<T> original,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos
	) throws Throwable
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				return original.call(instance, property);
			}
			catch (Throwable throwable)
			{
				if (throwable instanceof UnknownServiceException || throwable instanceof IllegalArgumentException)
				{
					throw UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, world, pos);
				}
				else
				{
					throw throwable;
				}
			}
		}
		else
		{
			// vanilla
			return original.call(instance, property);
		}
	}
}
