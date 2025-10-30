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

package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.commands.PlayerCommand;
import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.carpet.playerActionEnhanced.PlayerActionPackHelper;
import carpettisaddition.helpers.carpet.playerActionEnhanced.randomly.RandomizedActionIntervalCommand;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

//#if MC >= 12000
//$$ import java.util.function.Consumer;
//#endif

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	//#if MC >= 12000
	//$$ @Shadow(remap = false)
	//$$ private static int manipulate(CommandContext<ServerCommandSource> context, Consumer<EntityPlayerActionPack> action)
	//$$ {
	//$$ 	return 0;
	//$$ }
	//#else
	@Shadow(remap = false)
	private static int action(CommandContext<CommandSourceStack> context, EntityPlayerActionPack.ActionType type, EntityPlayerActionPack.Action action)
	{
		return 0;
	}
	//#endif

	/**
	 * fabric carpet 1.4.104 makes a refactor on PlayerCommand and removes the {@link PlayerCommand#action} method.
	 * We need to use our own copy, so we don't have to change the usage in the {@link #applyPlayerActionEnhancements} below
	 */
	@Unique
	private static int action$TISCM(CommandContext<CommandSourceStack> context, EntityPlayerActionPack.ActionType type, EntityPlayerActionPack.Action action)
	{
		//#if MC >= 12000
		//$$ return manipulate(context, ap -> ap.start(type, action));
		//#else
		return action(context, type, action);
		//#endif
	}

	@Inject(method = "makeActionCommand", at = @At("RETURN"), remap = false)
	private static void applyPlayerActionEnhancements(String actionName, EntityPlayerActionPack.ActionType type, CallbackInfoReturnable<LiteralArgumentBuilder<CommandSourceStack>> cir)
	{
		RandomizedActionIntervalCommand.getInstance().extendCommand(cir.getReturnValue(), type, PlayerCommandMixin::action$TISCM);

		final String delay = "delay";
		final String perTick = "perTick";

		cir.getReturnValue().
				then(literal("after").
						then(argument(delay, integer(1)).
								executes(
										c -> action$TISCM(c, type, PlayerActionPackHelper.after(getInteger(c, delay)))
								)
						)
				).
				then(literal("perTick").
						then(argument(perTick, integer(1, 64)).
								requires(CarpetModUtil::canUseCarpetCommand).
								executes(
										c -> handlePerTick$TISCM(c, type, getInteger(c, perTick))
								)
						)
				);
	}

	@Unique
	private static int handlePerTick$TISCM(CommandContext<CommandSourceStack> context, EntityPlayerActionPack.ActionType type, int perTick)
	{
		if (!CarpetModUtil.canUseCommand(context.getSource(), CarpetTISAdditionSettings.commandPlayerActionPerTick))
		{
			Messenger.tell(context.getSource(), Messenger.formatting(Messenger.tr("carpettisaddition.command.player.action.perTick.disabled"), ChatFormatting.GOLD));
			return 0;
		}

		return action$TISCM(context, type, PlayerActionPackHelper.perTick(perTick));
	}
}
