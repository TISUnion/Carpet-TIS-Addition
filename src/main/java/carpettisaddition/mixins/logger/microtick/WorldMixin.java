package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.interfaces.IWorld_MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld_MicroTickLogger
{
	private MicroTickLogger microTickLogger;

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTickLogger = new MicroTickLogger((World)(Object)this);
	}

	public MicroTickLogger getMicroTickLogger()
	{
		return this.microTickLogger;
	}
}
