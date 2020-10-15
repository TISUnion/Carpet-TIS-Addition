package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.interfaces.IWorld_MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld_MicroTickLogger
{
	@Shadow @Final protected LevelProperties properties;
	private MicroTickLogger microTickLogger;

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTickLogger = new MicroTickLogger((World)(Object)this);
	}

	@Inject(
			method = "tickTime",
			at = @At(value = "HEAD")
	)
	private void onTimeUpdate(CallbackInfo ci)
	{
		if (this.properties.getClass() == LevelProperties.class)  // only flush messages main world time update
		{
			MicroTickLoggerManager.flushMessages();
		}
	}

	@Override
	public MicroTickLogger getMicroTickLogger()
	{
		return this.microTickLogger;
	}
}
