/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.tweaks.command.fakePlayerRejoin;

import carpet.commands.PlayerCommand;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.server.command.CommandManager.literal;

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	@Shadow(remap = false)
	private static int spawn(CommandContext<ServerCommandSource> context)
	{
		return 0;
	}

	@ModifyReceiver(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;suggests(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;",
					ordinal = 0,
					remap = false
			),
			remap = false
	)
	private static RequiredArgumentBuilder<ServerCommandSource, ?> fakePlayerRejoin_addRejoinCommand(RequiredArgumentBuilder<ServerCommandSource, ?> node, SuggestionProvider<?> provider)
	{
		return node.then(literal("rejoin").executes(PlayerCommandMixin::rejoin));
	}

	@Unique
	private static int rejoin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		FakePlayerRejoinHelper.isRejoin.set(true);
		try
		{
			return spawn(context);
		}
		finally
		{
			FakePlayerRejoinHelper.isRejoin.remove();
		}
	}
}
