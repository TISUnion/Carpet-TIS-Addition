package carpettisaddition.mixins.option.opPlayerNoCheat;

import carpettisaddition.helpers.CommandPermissionHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SetBlockCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Stop pasting schematic in SMP
@Mixin(SetBlockCommand.class)
public abstract class SetBlockCommandMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(
			method = "method_13627",  // lambda method
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z"
			),
			allow = 1
	)
	private static boolean checkIfAllowCheating(ServerCommandSource source, int level)
	{
		return CommandPermissionHelper.canCheat(source, level);
	}
}
