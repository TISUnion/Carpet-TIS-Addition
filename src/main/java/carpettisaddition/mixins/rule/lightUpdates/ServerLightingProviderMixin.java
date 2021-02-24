package carpettisaddition.mixins.rule.lightUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
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
	private final ThreadLocal<Boolean> enqueueImportant = ThreadLocal.withInitial(() -> false);

	@Inject(
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$class_3901;Ljava/lang/Runnable;)V",
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

	@Inject(method = "runTasks", at = @At(value = "HEAD"), cancellable = true)
	void onExecutingLightUpdates(CallbackInfo ci)
	{
		if (!CarpetTISAdditionSettings.lightUpdates.shouldExecuteLightTask())
		{
			ci.cancel();
		}
	}

	@Inject(method = "light", at = @At("HEAD"))
	private void dummyInjectionForObfRefMap(CallbackInfoReturnable<CompletableFuture<Chunk>> cir)
	{
	}

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
}
