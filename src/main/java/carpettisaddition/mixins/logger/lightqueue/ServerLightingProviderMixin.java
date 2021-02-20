package carpettisaddition.mixins.logger.lightqueue;

import carpettisaddition.logging.loggers.lightqueue.IServerLightingProvider;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin implements IServerLightingProvider
{
	@Shadow @Final private ObjectList<Pair<ServerLightingProvider.Stage, Runnable>> pendingTasks;

	private long enqueuedTaskCount;
	private long executedTaskCount;

	@Inject(
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$Stage;Ljava/lang/Runnable;)V",
			at = @At(value = "TAIL"),
			cancellable = true
	)
	void onEnqueuedLightUpdateTask(CallbackInfo ci)
	{
		this.enqueuedTaskCount++;
	}

	@Inject(
			method = "runTasks",
			at = @At(value = "TAIL"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	void onExecutingLightUpdates(CallbackInfo ci, int i)
	{
		this.executedTaskCount += i;
	}

	@Override
	public long getEnqueuedTaskCount()
	{
		return this.enqueuedTaskCount;
	}

	@Override
	public long getExecutedTaskCount()
	{
		return this.executedTaskCount;
	}

	@Override
	public ObjectList<Pair<ServerLightingProvider.Stage, Runnable>> getTaskQueue()
	{
		return this.pendingTasks;
	}
}
