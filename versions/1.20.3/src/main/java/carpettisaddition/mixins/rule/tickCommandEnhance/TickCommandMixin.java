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

package carpettisaddition.mixins.rule.tickCommandEnhance;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	/**
	 * Hook at the `literal("sprint")` object
	 */
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
					target = "Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;",
					ordinal = 0
			)
	)
	private static LiteralArgumentBuilder<ServerCommandSource> registerTickSprintInfoCommand(LiteralArgumentBuilder<ServerCommandSource> sprintNode)
	{
		TickWarpHUDLogger.getInstance().extendCommand(CommandTreeContext.ofNonContext(sprintNode));
		return sprintNode;
	}
}
