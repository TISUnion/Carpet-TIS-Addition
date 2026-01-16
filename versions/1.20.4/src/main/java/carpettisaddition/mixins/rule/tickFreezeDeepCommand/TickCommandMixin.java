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

package carpettisaddition.mixins.rule.tickFreezeDeepCommand;

import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
import carpettisaddition.helpers.rule.tickFreezeDeepCommand.DeepFreezableServerTickRateManager;
import carpettisaddition.utils.Messenger;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.commands.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

import static net.minecraft.commands.Commands.literal;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	@Shadow
	private static int setFreeze(CommandSourceStack source, boolean frozen)
	{
		throw new AssertionError();
	}

	@Unique
	private static boolean currentFreezeIsDeepFreeze$TISCM = false;

	@ModifyExpressionValue(
			method = "register",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=freeze"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/commands/Commands;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;",
					ordinal = 0
			)
	)
	private static LiteralArgumentBuilder<CommandSourceStack> tickFreezeDeepCommand_addCommand(LiteralArgumentBuilder<CommandSourceStack> freezeNode)
	{
		return freezeNode.then(literal("deep").
				requires(s -> TickCommandCarpetfiedRules.tickFreezeDeepCommand()).
				executes(c -> {
					currentFreezeIsDeepFreeze$TISCM = true;
					try
					{
						return setFreeze(c.getSource(), true);
					}
					finally
					{
						currentFreezeIsDeepFreeze$TISCM = false;
					}
				})
		);
	}

	@Inject(
			method = "setFreeze",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/ServerTickRateManager;setFrozen(Z)V",
					shift = At.Shift.AFTER
			)
	)
	private static void tickFreezeDeepCommand_setDeepFreeze(
			CallbackInfoReturnable<Integer> cir,
			@Local(argsOnly = true) boolean frozen,
			@Local ServerTickRateManager serverTickRateManager
	)
	{
		if (currentFreezeIsDeepFreeze$TISCM)
		{
			if (frozen)
			{
				((DeepFreezableServerTickRateManager)serverTickRateManager).setDeeplyFrozen$TISCM(true);
			}
		}
	}

	@Unique
	private static Component wrapFeedbackForDeepFreeze(Component message)
	{
		return Messenger.tr(
				"carpettisaddition.rule.tickFreezeDeepCommand.wrapper",
				message,
				Messenger.formatting(Messenger.tr("carpettisaddition.rule.tickFreezeDeepCommand.deep_freeze"), ChatFormatting.DARK_AQUA)
		);
	}

	@ModifyArg(
			method = "setFreeze",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V",
					ordinal = 0
			)
	)
	private static Supplier<Component> tickFreezeDeepCommand_changeSetFreezeFeedbackMessage(
			Supplier<Component> messageSupplier,
			@Local(argsOnly = true) boolean frozen
	)
	{
		if (currentFreezeIsDeepFreeze$TISCM)
		{
			if (frozen)
			{
				final Supplier<Component> oldSupplier = messageSupplier;
				messageSupplier = () -> wrapFeedbackForDeepFreeze(oldSupplier.get());
			}
		}
		return messageSupplier;
	}

	@ModifyArg(
			method = "tickQuery",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/ServerTickRateManager;isFrozen()Z",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V",
					ordinal = 0
			)
	)
	private static Supplier<Component> tickFreezeDeepCommand_changeTickQueryFeedbackMessage(
			Supplier<Component> messageSupplier,
			@Local ServerTickRateManager serverTickRateManager
	)
	{
		if (((DeepFreezableServerTickRateManager)serverTickRateManager).isDeeplyFrozen$TISCM())
		{
			final Supplier<Component> oldSupplier = messageSupplier;
			messageSupplier = () -> wrapFeedbackForDeepFreeze(oldSupplier.get());
		}
		return messageSupplier;
	}
}
