package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.commands.PlayerCommand;
import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.playerActionEnhanced.PlayerActionPackHelper;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	@Shadow(remap = false)
	private static int action(CommandContext<ServerCommandSource> context, EntityPlayerActionPack.ActionType type, EntityPlayerActionPack.Action action)
	{
		return 0;
	}

	@Inject(method = "makeActionCommand", at = @At("RETURN"), remap = false)
	private static void addRandomlyArgument(String actionName, EntityPlayerActionPack.ActionType type, CallbackInfoReturnable<LiteralArgumentBuilder<ServerCommandSource>> cir)
	{
		final String lower = "lower_bound";
		final String upper = "upper_bound";
		final String delay = "delay";

		cir.getReturnValue().
				then(literal("randomly").
						then(argument(lower, integer(1)).
								then(argument(upper, integer(1)).
										executes(
												c -> action(c, type, PlayerActionPackHelper.randomly(getInteger(c, lower), getInteger(c, upper)))
										)
								)
						)
				).
				then(literal("after").
						then(argument(delay, integer(1)).
								executes(
										c -> action(c, type, PlayerActionPackHelper.after(getInteger(c, delay)))
								)
						)
				);
	}
}
