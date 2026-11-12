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

package carpettisaddition.mixins.rule.tickWarpCommandAsAnAlias;

import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
import carpettisaddition.utils.command.CommandNodeUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static net.minecraft.commands.Commands.literal;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;",
					remap = false
			)
	)
	private static LiteralArgumentBuilder<CommandSourceStack> addTickWarpNode(LiteralArgumentBuilder<CommandSourceStack> rootNode)
	{
		// Redirect to the node already attached to the command tree.
		CommandNode<CommandSourceStack> sprintNode = rootNode.getArguments().stream().
				filter(node -> node.getName().equals("sprint")).
				findFirst().orElseThrow(() -> new IllegalStateException("Missing /tick sprint command node"));
		rootNode.then(
				literal("warp").
				requires(CommandNodeUtils.requireCondition(TickCommandCarpetfiedRules::tickWarpCommandAsAnAlias)).
				redirect(sprintNode)
		);
		return rootNode;
	}
}
