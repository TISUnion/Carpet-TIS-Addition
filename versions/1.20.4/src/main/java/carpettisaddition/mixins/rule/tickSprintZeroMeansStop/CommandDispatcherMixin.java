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
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc >= 1.20.3: subproject 1.20.4                 <--------
 * mc <  1.20.3: subproject 1.15.2 (main project)
 */
@Mixin(value = CommandDispatcher.class, remap = false)
public abstract class CommandDispatcherMixin<S>
{
	@ModifyExpressionValue(method = "parseNodes", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/tree/CommandNode;getCommand()Lcom/mojang/brigadier/Command;"))
	private Command<S> tickSprintZeroMeansStop_selectStopCommand(Command<S> original, @Local(name = "child") CommandNode<S> childNode, @Local(name = "context") CommandContextBuilder<S> context)
	{
		if (!TickSprintShortcuts.isEnabledForSource(context.getSource()))
		{
			return original;
		}

		var dispatcher = context.getDispatcher();
		var sprintNode = dispatcher.findNode(TickSprintShortcuts.SPRINT_PATH);
		if (sprintNode == null)
		{
			return original;
		}
		if (original == null && (childNode == sprintNode || (TickSprintShortcuts.isWarpAlias(childNode, sprintNode) && childNode == dispatcher.findNode(TickSprintShortcuts.WARP_PATH))))
		{
			return TickSprintShortcuts.getSprintStopCommand(sprintNode, context.getSource());
		}
		if (childNode == sprintNode.getChild("time") && childNode instanceof ArgumentCommandNode<S, ?> timeNode && TickSprintShortcuts.isTimeArgument(timeNode.getType()))
		{
			var parsedTime = context.getArguments().get("time");
			if (parsedTime != null && Integer.valueOf(0).equals(parsedTime.getResult()))
			{
				return TickSprintShortcuts.getSprintStopCommand(sprintNode, context.getSource());
			}
		}
		return original;
	}
}
