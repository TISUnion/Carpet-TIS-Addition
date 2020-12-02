package carpettisaddition.mixins.logger.commandblock;

import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlockMinecartEntity.class)
public abstract class CommandBlockMinecartEntityMixin
{
	@Inject(
			method = "onActivatorRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/CommandBlockExecutor;execute(Lnet/minecraft/world/World;)Z",
					shift = At.Shift.AFTER
			)
	)
	private void onCommandBlockMinecartExecutedCommandBlockLogger(CallbackInfo ci)
	{
		CommandBlockLogger.getInstance().onCommandBlockMinecartActivated((CommandBlockMinecartEntity)(Object)this);
	}
}
