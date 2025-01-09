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
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Set;

//#if MC < 12105
import net.minecraft.server.command.SetBlockCommand;
//#endif

@Mixin(FillCommand.Mode.class)
public class FillCommandModeMixin
{
	static
	{
		fillSoftReplaceModeHijack();
	}

	private static <T extends Comparable<T>> void fillSoftReplaceModeHijack()
	{
		FillCommand.Mode replaceMode = FillCommand.Mode.REPLACE;
		//#if MC >= 12105
		//$$ FillCommand.Filter
		//#else
		SetBlockCommand.Filter
		//#endif
				oldFilter = replaceMode.filter;

		((FillCommandModeAccessor)(Object)replaceMode).setFilter((box, pos, blockStateArgument, world) -> {
			if (!FillModeEnhanceContext.isSoftReplace.get())
			{
				return oldFilter.filter(box, pos, blockStateArgument, world);
			}

			BlockState existedBlockState = world.getBlockState(pos);
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

			return new BlockStateArgument(mergedBlockState, mergedProperties, ((BlockStateArgumentAccessor)blockStateArgument).getData());
		});
	}
}
