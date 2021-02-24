package carpettisaddition.mixins.rule.opPlayerNoCheat;

import carpettisaddition.helpers.CommandPermissionHelper;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Stop F3 + N
@Mixin(GameModeCommand.class)
public abstract class GameModeCommandMixin
{
	@Dynamic
	@SuppressWarnings("DefaultAnnotationParam")
	@Redirect(
			method = "method_13389",  // lambda method
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z",
					remap = true
			),
			allow = 1,
			remap = false
	)
	private static boolean checkIfAllowCheating(ServerCommandSource source, int level)
	{
		return CommandPermissionHelper.canCheat(source, level);
	}
}
