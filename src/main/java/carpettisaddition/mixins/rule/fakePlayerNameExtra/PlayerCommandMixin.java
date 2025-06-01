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

package carpettisaddition.mixins.rule.fakePlayerNameExtra;

import carpet.commands.PlayerCommand;
import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

//#if MC < 11500
//$$ import carpet.utils.Messenger;
//$$ import net.minecraft.server.command.ServerCommandSource;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	private static String getDecoratedString(final CommandContext<?> context, final String name)
	{
		//#if MC >= 11700
		//$$ String playerName = StringArgumentType.getString(context, name);
		//$$ if (!name.equals("player"))
		//$$ {
		//$$ 	// since carpet v1.4.48 or whatever:
		//$$ 	// not <player> argument, might be <gamemode>, so return the value directly
		//$$ 	return playerName;
		//$$ }
		//#else
		String playerName = StringArgumentType.getString(context, name);
		//#endif

		String rulePrefix = CarpetTISAdditionSettings.fakePlayerNamePrefix;
		String ruleSuffix = CarpetTISAdditionSettings.fakePlayerNameSuffix;
		if (!rulePrefix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra) && !playerName.startsWith(rulePrefix))
		{
			playerName = rulePrefix + playerName;
		}
		if (!ruleSuffix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra) && !playerName.endsWith(ruleSuffix))
		{
			playerName = playerName + ruleSuffix;
		}
		return playerName;
	}

	@ModifyExpressionValue(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
			),
			//#if MC < 11700
			require = 2,
			//#endif
			remap = false
	)
	private static String getStringWithPrefixAtSpawn(String name, @Local(argsOnly = true) CommandContext<?> context)
	{
		return getDecoratedString(context, name);
	}

	@ModifyExpressionValue(
			method = "cantSpawn",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
			),
			require = 1,
			remap = false
	)
	private static String getStringWithPrefixAtCantSpawn(String name, @Local(argsOnly = true) CommandContext<?> context)
	{
		return getDecoratedString(context, name);
	}

	//#if MC >= 11600
	//#elseif MC >= 11500
	@ModifyExpressionValue(
			method = "spawn",
			at = @At(
					value = "CONSTANT",
					args = "intValue=40"
			),
			require = 1,
			remap = false
	)
	private static int nameLengthLimit(int value)
	{
		return 16;
	}
	//#else
	//$$ @Inject(
	//$$		method = "spawn",
	//$$ 		at = @At(value = "HEAD"),
	//$$ 		remap = false,
	//$$ 		cancellable = true
	//$$ )
	//$$ private static void checkNameLengthLimit(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir)
	//$$ {
	//$$ 	String playerName = getDecoratedString(context, "player");
	//$$ 	if (playerName.length() > 16)
	//$$ 	{
	//$$ 		Messenger.m(context.getSource(), "rb Player name: " + playerName + " is too long");
	//$$ 		cir.setReturnValue(1);
	//$$ 	}
	//$$ }
	//#endif
}
