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

package carpettisaddition.mixins.command.fill.modeenhance;

import carpettisaddition.commands.fill.modeenhance.FillModeEnhanceContext;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;

@Mixin(FillCommand.Mode.class)
public class FillCommandModeMixin
{
	@Inject(
			method = "method_13358",  // lambda method in constructor parameter of FillCommand.Mode.REPLACE
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static <T extends Comparable<T>> void fillSoftReplaceLogic(BlockBox blockBox, BlockPos blockPos, BlockStateArgument blockStateArgument, ServerWorld serverWorld, CallbackInfoReturnable<BlockStateArgument> cir)
	{
		if (FillModeEnhanceContext.isSoftReplace.get())
		{
			BlockState existedBlockState = serverWorld.getBlockState(blockPos);
			BlockState targetBlockState = blockStateArgument.getBlockState();
			Collection<Property<?>> existedProperties = existedBlockState.getProperties();
			Collection<Property<?>> targetProperties = targetBlockState.getProperties();
			Set<Property<?>> specifiedProperties = ((BlockStateArgumentAccessor)blockStateArgument).getProperties();

			BlockState mergedBlockState = targetBlockState;
			Set<Property<?>> mergedProperties = Sets.newHashSet(specifiedProperties);
			for (Property<?> property1 : existedProperties)
			{
				@SuppressWarnings("unchecked")
				Property<T> property = (Property<T>)property1;

				if (targetProperties.contains(property) && !specifiedProperties.contains(property))
				{
					mergedBlockState = mergedBlockState.with(property, existedBlockState.get(property));
					mergedProperties.add(property);
				}
			}

			cir.setReturnValue(new BlockStateArgument(mergedBlockState, mergedProperties, ((BlockStateArgumentAccessor)blockStateArgument).getData()));
		}
	}
}
