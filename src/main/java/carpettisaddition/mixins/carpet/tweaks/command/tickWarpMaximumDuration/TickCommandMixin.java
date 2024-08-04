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

package carpettisaddition.mixins.carpet.tweaks.command.tickWarpMaximumDuration;

import carpet.commands.TickCommand;
import carpettisaddition.utils.ModIds;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = "<1.20.3"),
		@Condition(value = ModIds.carpet, versionPredicates = "<1.4.18"),
})
@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	@Dynamic
	@Redirect(
			method = "register",
			// should be accurate enough
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=warp",
							ordinal = 0
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=tail command",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/IntegerArgumentType;integer(II)Lcom/mojang/brigadier/arguments/IntegerArgumentType;",
					ordinal = 0
			),
			require = 0,
			remap = false
	)
	private static IntegerArgumentType restrictInRange(int min, int max)
	{
		// fabric carpet removed the 4000000 upper limit in version 1.4.18
		// so here's an extra check to make sure it's what we want
		if (min == 0 && max == 4000000)
		{
			max = Integer.MAX_VALUE;
		}
		return IntegerArgumentType.integer(min, max);
	}

}
