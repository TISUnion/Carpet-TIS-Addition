package carpettisaddition.mixins.command.stop;

import carpettisaddition.commands.stop.StopCommandDoubleConfirmation;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.StopCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StopCommand.class)
public abstract class StopCommandMixin
{
	@Inject(
			method = "method_13676",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private static void stopCommandDoubleConfirmation(CommandContext<ServerCommandSource> commandContext, CallbackInfoReturnable<Integer> cir)
	{
		StopCommandDoubleConfirmation.handleDoubleConfirmation(commandContext, cir);
	}
}
