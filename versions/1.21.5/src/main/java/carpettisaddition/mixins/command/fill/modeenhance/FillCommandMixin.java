/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.fill.modeenhance.FillSoftReplaceCommand;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc1.14 ~ mc1.21.4: subproject 1.15.2 (main project)
 * mc1.21.5+        : subproject 1.21.5        <--------
 */
@Mixin(FillCommand.class)
public abstract class FillCommandMixin
{
	@ModifyReturnValue(method = "buildModeTree", at = @At("TAIL"))
	private static ArgumentBuilder<ServerCommandSource, ?> registerSoftReplaceFillMode(
			ArgumentBuilder<ServerCommandSource, ?> node,
			@Local(argsOnly = true) CommandRegistryAccess currentCommandBuildContext
	)
	{
		FillSoftReplaceCommand.getInstance().extendCommand(CommandTreeContext.of(node, currentCommandBuildContext));
		return node;
	}
}
