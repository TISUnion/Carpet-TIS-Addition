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

package carpettisaddition.mixins.command.info;

import carpet.commands.InfoCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.info.InfoCommandExtension;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11900
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

@Mixin(InfoCommand.class)
public abstract class InfoCommandMixin
{
	@ModifyVariable(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			ordinal = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<ServerCommandSource> extendsInfoCommand(
			LiteralArgumentBuilder<ServerCommandSource> builder
			//#if MC >= 11900
			//$$ , CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		InfoCommandExtension.getInstance().extendCommand(
				CommandTreeContext.of(
						builder
						//#if MC >= 11900
						//$$ , commandBuildContext
						//#endif
				)
		);
		return builder;
	}
}
