package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;


@Mixin(ServerTickScheduler.class)
public abstract class ServerTickSchedulerMixin<T>
{
	@Shadow @Final private ServerWorld world;

	@Shadow @Final private Set<ScheduledTick<T>> scheduledTickActions;
	private int oldListSize;

	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	{
		this.oldListSize = this.scheduledTickActions.size();
	}

	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		if (object instanceof Block)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(this.world, (Block)object, pos, delay, priority, this.scheduledTickActions.size() > oldListSize);
		}
	}
}
