package carpettisaddition.mixins.rule.lightUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

// method light is overwritten by mc-fix_mc-170012 with priority 1000, so the priority needs to be at least 1000
@Mixin(value = ServerLightingProvider.class, priority = 1500)
public abstract class ServerLightingProviderMixin
{
	@Shadow protected abstract void runTasks();

	private final ThreadLocal<Boolean> enqueueImportant = ThreadLocal.withInitial(() -> false);
	private long passedRunTaskedCount = 0L;
	private int runTaskDepth = 0;
	private final Object passedRunTaskedCountLock = 0L;

	@Inject(
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$Stage;Ljava/lang/Runnable;)V",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	void onEnqueueingLightUpdateTask(CallbackInfo ci)
	{
		if (this.enqueueImportant.get())
		{
			this.enqueueImportant.set(false);
			return;
		}
		if (!CarpetTISAdditionSettings.lightUpdates.shouldEnqueueLightTask())
		{
			ci.cancel();
		}
	}

	/**
	 * To implement a "suppressed" effect, you can't just enqueue a custom task with infinity executing time into to the
	 * task queue of the light thread, or you will not be able to let those important enqueue tasks (e.g. those async
	 * futures when player enters a not-loaded chunk) to execute, since it's a queue
	 * So I just let the tasks of the light thread run, but make those tasks do nothing
	 */
	@Inject(method = "runTasks", at = @At(value = "HEAD"), cancellable = true)
	void onExecutingLightUpdates(CallbackInfo ci)
	{
		if (!CarpetTISAdditionSettings.lightUpdates.shouldExecuteLightTask())
		{
			synchronized (this.passedRunTaskedCountLock)
			{
				this.passedRunTaskedCount++;
			}
			ci.cancel();
		}
	}

	@Inject(method = "light", at = @At("HEAD"))
	private void dummyInjectionForObfRefMap(CallbackInfoReturnable<CompletableFuture<Chunk>> cir)
	{
	}

	/**
	 * If you dont want to block the server forever when player entering not-loaded chunks
	 */
	@Dynamic
	@Group(min = 1, max = 2)
	@Inject(
			method = {
					"light",  // its ref map is generated in the injection "dummyInjectionForObfRefMap" above
					"setupLightmaps"  // mc-fix_mc-170012
			},
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
					remap = false
			),
			remap = false,
			require = 0
	)
	private void thisEnqueueIsImportant(CallbackInfoReturnable<CompletableFuture<Chunk>> cir)
	{
		this.enqueueImportant.set(true);
	}

	/**
	 * To make sure tasks left behind due to lightUpdates values with shouldExecuteLightTask() false, are getting
	 * processed again quick enough
	 */
	@Inject(method = "runTasks", at = @At("TAIL"))
	private void runMoreTaskToCatchUpIfNecessary(CallbackInfo ci)
	{
		if (this.runTaskDepth > 0)
		{
			return;
		}

		this.runTaskDepth++;
		try
		{
			while (CarpetTISAdditionSettings.lightUpdates.shouldExecuteLightTask())
			{
				synchronized (this.passedRunTaskedCountLock)
				{
					if (this.passedRunTaskedCount <= 0)
					{
						break;
					}
					this.passedRunTaskedCount--;
				}
				this.runTasks();
			}
		}
		finally
		{
			this.runTaskDepth--;
		}
	}
}
