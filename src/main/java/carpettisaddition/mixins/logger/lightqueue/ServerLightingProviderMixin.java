package carpettisaddition.mixins.logger.lightqueue;

import carpettisaddition.logging.loggers.lightqueue.IServerLightingProvider;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicLong;

@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin implements IServerLightingProvider
{
	private final AtomicLong enqueuedTaskCount = new AtomicLong();
	private final AtomicLong executedTaskCount = new AtomicLong();
	private final AtomicLong queueSize = new AtomicLong();

	@Inject(
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$class_3901;Ljava/lang/Runnable;)V",
			at = @At(value = "TAIL")
	)
	void onEnqueuedLightUpdateTask(CallbackInfo ci)
	{
		this.enqueuedTaskCount.getAndIncrement();
		this.queueSize.getAndIncrement();
	}

	@Inject(
			method = "runTasks",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/ObjectListIterator;remove()V",
					remap = false
			)
	)
	void onExecutedLightUpdates(CallbackInfo ci)
	{
		this.executedTaskCount.getAndIncrement();
		this.queueSize.getAndDecrement();
	}

	@Override
	public long getEnqueuedTaskCountAndClean()
	{
		return this.enqueuedTaskCount.getAndSet(0);
	}

	@Override
	public long getExecutedTaskCountAndClean()
	{
		return this.executedTaskCount.getAndSet(0);
	}

	@Override
	public long getQueueSize()
	{
		return this.queueSize.get();
	}
}
