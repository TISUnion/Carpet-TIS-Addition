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
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$Stage;Ljava/lang/Runnable;)V",
			at = @At(value = "TAIL"),
			cancellable = true
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
	public long getEnqueuedTaskCount()
	{
		return this.enqueuedTaskCount.get();
	}

	@Override
	public long getExecutedTaskCount()
	{
		return this.executedTaskCount.get();
	}

	@Override
	public void resetCounter()
	{
		this.enqueuedTaskCount.set(0);
		this.executedTaskCount.set(0);
	}

	@Override
	public long getQueueSize()
	{
		return this.queueSize.get();
	}
}
