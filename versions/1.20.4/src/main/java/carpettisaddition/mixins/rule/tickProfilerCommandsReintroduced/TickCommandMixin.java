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

package carpettisaddition.mixins.rule.tickProfilerCommandsReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static carpet.commands.ProfileCommand.healthEntities;
import static carpet.commands.ProfileCommand.healthReport;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
	private static LiteralArgumentBuilder<ServerCommandSource> addCarpetProfileCommandNodes(LiteralArgumentBuilder<ServerCommandSource> rootNode)
	{
		addTickProfileCommandsAliases(rootNode);
		return rootNode;
	}

	/**
	 * Reference: {@link carpet.commands.ProfileCommand#register}
	 */
	@Unique
	private static void addTickProfileCommandsAliases(LiteralArgumentBuilder<ServerCommandSource> rootNode)
	{
		rootNode.
				then(literal("health").
						requires(s -> CarpetTISAdditionSettings.tickProfilerCommandsReintroduced).
						executes(c -> healthReport(c.getSource(), 100)).
						then(argument("ticks", integer(20, 24000)).
								executes((c) -> healthReport(c.getSource(), getInteger(c, "ticks")))
						)
				).
				then(literal("entities").
						requires(s -> CarpetTISAdditionSettings.tickProfilerCommandsReintroduced).
						executes(c -> healthEntities(c.getSource(), 100)).
						then(argument("ticks", integer(20, 24000)).
								executes(c -> healthEntities(c.getSource(), getInteger(c, "ticks")))
						)
				);
	}
}
