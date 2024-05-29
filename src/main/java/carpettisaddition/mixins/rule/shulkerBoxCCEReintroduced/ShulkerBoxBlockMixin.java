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

package carpettisaddition.mixins.rule.shulkerBoxCCEReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin
{
    @WrapMethod(method = "getComparatorOutput")
	private int shulkerBoxCCEReintroduced_castIt(BlockState state, World world, BlockPos pos, Operation<Integer> original)
    {
        try
        {
           return original.call(state, world, pos);
        }
        catch (ClassCastException classCastException)
        {
            if (CarpetTISAdditionSettings.shulkerBoxCCEReintroduced)
            {
                throw classCastException;
            }
            else
            {
                return Container.calculateComparatorOutput(world.getBlockEntity(pos));
            }
        }
    }
}
