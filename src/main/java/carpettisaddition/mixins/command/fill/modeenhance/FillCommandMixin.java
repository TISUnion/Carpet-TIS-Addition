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

package carpettisaddition.mixins.command.fill.modeenhance;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.fill.modeenhance.FillSoftReplaceCommand;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

//#if MC >= 1.19
//$$ import com.llamalad7.mixinextras.sugar.Local;
//$$ import net.minecraft.commands.CommandBuildContext;
//#endif

/**
 * mc1.14 ~ mc1.21.4: subproject 1.15.2 (main project)        <--------
 * mc1.21.5+        : subproject 1.21.5
 */
@Mixin(FillCommand.class)
public abstract class FillCommandMixin
{
	@ModifyArg(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=destroy",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
					remap = false,
					ordinal = 1
			)
	)
	private static ArgumentBuilder<CommandSourceStack, ?> registerSoftReplaceFillMode(
			ArgumentBuilder<CommandSourceStack, ?> node
			//#if MC >= 1.19
			//$$ , @Local(argsOnly = true) CommandBuildContext commandBuildContext
			//#endif
	)
	{
		FillSoftReplaceCommand.getInstance().extendCommand(
				CommandTreeContext.of(
						node
						//#if MC >= 1.19
						//$$ , commandBuildContext
						//#endif
				)
		);
		return node;
	}
}
