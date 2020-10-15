package carpettisaddition.mixins.logger.microtick.tickstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	@Shadow @Final private ServerWorld world;

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(
			method = "method_20801",  // lambda method
			at = @At(
					value = "INVOKE",
					args = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;isTooFarFromPlayersToSpawnMobs(Lnet/minecraft/util/math/ChunkPos;)Z"
			)
	)
	private void onStageSpawn(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage(this.world, "Spawning");
	}
	@Inject(
			method = "tickChunks",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(ZZ)V"
			)
	)
	private void onStageSpawnSpecial(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage(this.world, "SpawningSpecial");
	}
}
