package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@Shadow @Final private ServerWorldProperties worldProperties;

	@Inject(
			method = "tickTime",
			at = @At(value = "HEAD")
	)
	private void onTimeUpdate(CallbackInfo ci)
	{
		if (this.worldProperties.getClass() == LevelProperties.class)  // only flush messages main world time update
		{
			MicroTickLoggerManager.flushMessages();
		}
	}
}
