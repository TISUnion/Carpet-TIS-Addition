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

package carpettisaddition.helpers.rule.tickSprintZeroMeansStop;

import carpet.fakes.CommandNodeInterface;
import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.commands.arguments.TimeArgument;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

//#if MC >= 26.4
//$$ import net.minecraft.commands.ParseContext;
//$$ import net.minecraft.commands.arguments.SerializableArgument;
//$$ import net.minecraft.commands.suggestions.SuggestionContext;
//#else
import net.minecraft.commands.CommandSourceStack;
//#endif

/**
 * mc >= 1.20.3: subproject 1.20.4                 <--------
 * mc <  1.20.3: subproject 1.15.2 (main project)
 */
public class TickSprintShortcuts
{
	public static final List<String> SPRINT_PATH = List.of("tick", "sprint");
	public static final List<String> WARP_PATH = List.of("tick", "warp");

	public static boolean isEnabledForSource(Object source)
	{
		//#if MC >= 26.4
		// Carpet wraps server parse contexts. Client suggestion contexts have no server.
		//$$ return TickCommandCarpetfiedRules.tickSprintZeroMeansStop() && source instanceof ParseContext && (!(source instanceof SuggestionContext suggestions) || suggestions.server() != null);
		//#else
		return TickCommandCarpetfiedRules.tickSprintZeroMeansStop() && source instanceof CommandSourceStack;
		//#endif
	}

	public static boolean isTimeArgument(ArgumentType<?> type)
	{
		//#if MC >= 26.4
		//$$ return type instanceof SerializableArgument.BrigadierWrapper<?> wrapper && wrapper.argument() instanceof TimeArgument;
		//#else
		return type instanceof TimeArgument;
		//#endif
	}

	public static <S> @Nullable Command<S> getSprintStopCommand(CommandNode<S> sprintNode, S source)
	{
		var stopNode = sprintNode.getChild("stop");
		return stopNode != null && stopNode.canUse(source) ? stopNode.getCommand() : null;
	}

	public static boolean isWarpAlias(@Nullable CommandNode<?> node, CommandNode<?> sprintNode)
	{
		return node != null && node.getRedirect() == sprintNode && TickCommandCarpetfiedRules.tickWarpCommandAsAnAlias();
	}

	public static <S> void addShortcutsToClientTree(RootCommandNode<S> rootNode)
	{
		var tickNode = rootNode.getChild("tick");
		if (tickNode == null)
		{
			return;
		}
		var sprintNode = tickNode.getChild("sprint");
		var stopCommand = Optional.ofNullable(sprintNode)
				.map(node -> node.getChild("stop"))
				.map(CommandNode::getCommand)
				.orElse(null);
		if (stopCommand == null)
		{
			return;
		}

		// addChild merges the command into the existing sprint node, so redirects to it still work.
		if (sprintNode.getCommand() == null)
		{
			tickNode.addChild(LiteralArgumentBuilder.<S>literal("sprint").executes(stopCommand).build());
		}
		var warpNode = tickNode.getChild("warp");
		if (isWarpAlias(warpNode, sprintNode) && warpNode.getCommand() == null)
		{
			tickNode.addChild(LiteralArgumentBuilder.<S>literal("warp").executes(stopCommand).build());
		}

		if (sprintNode.getChild("time") instanceof ArgumentCommandNode<S, ?> timeNode && isTimeArgument(timeNode.getType()))
		{
			var replacementNode = new ArgumentCommandNode<>(
					timeNode.getName(), TimeArgument.time(0), timeNode.getCommand(), timeNode.getRequirement(),
					timeNode.getRedirect(), timeNode.getRedirectModifier(), timeNode.isFork(), timeNode.getCustomSuggestions()
			);
			timeNode.getChildren().forEach(replacementNode::addChild);
			((CommandNodeInterface)sprintNode).carpet$removeChild(timeNode.getName());
			sprintNode.addChild(replacementNode);
		}
	}
}
