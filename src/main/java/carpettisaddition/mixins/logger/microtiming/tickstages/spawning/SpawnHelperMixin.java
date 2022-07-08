package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11500
import net.minecraft.server.world.ServerWorld;
//#else
//$$ import net.minecraft.world.World;
//#endif

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	@ModifyVariable(method = "spawnEntitiesInChunk", at = @At("HEAD"), argsOnly = true)
	//#if MC >= 11500
	private static ServerWorld enterStageSpawn(ServerWorld world)
	//#else
	//$$ private static World enterStageSpawn(World world)
	//#endif
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.SPAWNING);
		return world;
	}

	@ModifyVariable(method = "spawnEntitiesInChunk", at = @At("TAIL"), argsOnly = true)
	//#if MC >= 11500
	private static ServerWorld exitStageSpawn(ServerWorld world)
	//#else
	//$$ private static World exitStageSpawn(World world)
	//#endif
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.UNKNOWN);
		return world;
	}
}
