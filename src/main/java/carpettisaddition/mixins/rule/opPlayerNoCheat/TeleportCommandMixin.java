package carpettisaddition.mixins.rule.opPlayerNoCheat;

import carpettisaddition.helpers.CommandPermissionHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Click and teleport in VoxelMap or whatever Minimap mod
@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin
{
	@Dynamic
	@SuppressWarnings("DefaultAnnotationParam")
	@Redirect(
			method = {"method_13763", "method_13764"},  // lambda method
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z",
					remap = true
			),
			allow = 2,
			remap = false
	)
	private static boolean checkIfAllowCheating(ServerCommandSource source, int level)
	{
		return CommandPermissionHelper.canCheat(source, level);
	}
}
