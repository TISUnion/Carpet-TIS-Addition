package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	/*
	 in mc < 1.14, the world arg is a World,
	 in mc >= 1.15, the world arg is a ServerWorld,
	 so we use a @Coerce for simplest compatibility
	*/

	@Inject(method = "spawnEntitiesInChunk", at = @At("HEAD"))
	private static void enterStageSpawn(EntityCategory category, @Coerce World world, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.SPAWNING);
	}

	@Inject(method = "spawnEntitiesInChunk", at = @At("TAIL"))
	private static void exitStageSpawn(EntityCategory category, @Coerce World world, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.UNKNOWN);
	}
}
