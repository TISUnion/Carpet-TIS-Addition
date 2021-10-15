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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	@Inject(method = "spawnEntitiesInChunk", at = @At("HEAD"))
	private static void enterStageSpawn(EntityCategory category, World world, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.SPAWNING);
	}

	@Inject(method = "spawnEntitiesInChunk", at = @At("TAIL"))
	private static void exitStageSpawn(EntityCategory category, World world, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.UNKNOWN);
	}
}
