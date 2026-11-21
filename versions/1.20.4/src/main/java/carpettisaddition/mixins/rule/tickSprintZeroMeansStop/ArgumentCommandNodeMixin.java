/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.tickSprintZeroMeansStop;

import carpettisaddition.helpers.rule.tickSprintZeroMeansStop.TickSprintShortcuts;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.minecraft.commands.arguments.TimeArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc >= 1.20.3: subproject 1.20.4                 <--------
 * mc <  1.20.3: subproject 1.15.2 (main project)
 */
@Mixin(value = ArgumentCommandNode.class, remap = false)
public abstract class ArgumentCommandNodeMixin<S>
{
	@ModifyExpressionValue(method = "parse", at = @At(value = "FIELD", target = "Lcom/mojang/brigadier/tree/ArgumentCommandNode;type:Lcom/mojang/brigadier/arguments/ArgumentType;"))
	private ArgumentType<?> tickSprintZeroMeansStop_allowZeroSprintTime(ArgumentType<?> original, @Local(argsOnly = true) CommandContextBuilder<S> context)
	{
		if (TickSprintShortcuts.isEnabledForSource(context.getSource()) && TickSprintShortcuts.isTimeArgument(original))
		{
			var sprintNode = context.getDispatcher().findNode(TickSprintShortcuts.SPRINT_PATH);
			if (sprintNode != null && sprintNode.getChild("time") == (Object)this && TickSprintShortcuts.getSprintStopCommand(sprintNode, context.getSource()) != null)
			{
				return TimeArgument.time(0);
			}
		}
		return original;
	}
}
