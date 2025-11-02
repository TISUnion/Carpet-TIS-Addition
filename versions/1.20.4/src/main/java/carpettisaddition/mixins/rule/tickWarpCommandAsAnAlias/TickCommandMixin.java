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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static net.minecraft.commands.Commands.literal;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	private static LiteralArgumentBuilder<CommandSourceStack> sprintNode$TISCM = null;

	@ModifyExpressionValue(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=sprint"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/commands/Commands;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;",
					ordinal = 0
			)
	)
	private static LiteralArgumentBuilder<CommandSourceStack> storeTheSprintNode(LiteralArgumentBuilder<CommandSourceStack> sprintNode)
	{
		sprintNode$TISCM = sprintNode;
		return sprintNode;
	}

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
		addTickWarpAlias(rootNode);
		return rootNode;
	}

	@Unique
	private static void addTickWarpAlias(LiteralArgumentBuilder<CommandSourceStack> rootNode)
	{
		rootNode.then(
				literal("warp").
				requires(s -> TickCommandCarpetfiedRules.tickWarpCommandAsAnAlias()).
				redirect(sprintNode$TISCM.build())
		);
	}
}
