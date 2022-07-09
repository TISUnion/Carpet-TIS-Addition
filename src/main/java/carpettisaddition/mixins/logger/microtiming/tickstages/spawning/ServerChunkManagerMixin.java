package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
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
	@Shadow @Final
	//#if MC < 11700
	private
	//#endif
	ServerWorld world;

	@Inject(
			method = "tickChunks",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(ZZ)V"
					//#else
					target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;spawnEntities(Lnet/minecraft/server/world/ServerWorld;ZZ)V"
					//#endif
			)
	)
	private void enterStageSpawnSpecial(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.SPAWNING_SPECIAL);
	}

	@Inject(
			method = "tickChunks",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(ZZ)V",
					//#else
					target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;spawnEntities(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void exitStageSpawnSpecial(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.UNKNOWN);
	}
}
