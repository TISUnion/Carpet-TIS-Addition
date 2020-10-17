package carpettisaddition.mixins.logger.microtick.tickstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.TickStage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	@Inject(method = "spawn", at = @At("HEAD"))
	private static void onStageSpawn(ServerWorld world, WorldChunk chunk, SpawnHelper.Info info, boolean spawnAnimals, boolean spawnMonsters, boolean shouldSpawnAnimals, CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage(world, TickStage.SPAWNING);
	}
}
