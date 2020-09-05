package carpettisaddition.mixins.option.opPlayerNoCheat;

import carpettisaddition.helpers.CommandPermissionHelper;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Click and teleport in VoxelMap or whatever Minimap mod
@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(
			method = {"method_13763", "method_13764"},  // lambda method
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z"
			),
			allow = 2
	)
	private static boolean checkIfAllowCheating(ServerCommandSource source, int level)
	{
		return CommandPermissionHelper.canCheat(source, level);
	}
}
