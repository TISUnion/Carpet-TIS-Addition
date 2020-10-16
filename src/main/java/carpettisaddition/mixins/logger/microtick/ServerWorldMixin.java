package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.interfaces.IServerWorld_MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements IServerWorld_MicroTickLogger
{
	private MicroTickLogger microTickLogger;

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTickLogger = new MicroTickLogger((ServerWorld)(Object)this);
	}

	@Override
	public MicroTickLogger getMicroTickLogger()
	{
		return this.microTickLogger;
	}
}
