package carpettisaddition.mixins.command.lifetime.spawning.natural;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// use smaller priority so it @ModifyArg before fabric carpet's @Redirect
@Mixin(value = SpawnHelper.class, priority = 500)
public abstract class SpawnHelperMixin
{
	// fabric-carpet used @Redirect so there goes @ModifyArg here xd
	// don't try to capture locals, WAY too many
	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
			//#else
			method = "spawnEntitiesInChunk",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
					//#elseif MC >= 11500
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#else
					//$$ target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			),
			index = 0,
			allow = 1
	)
	private static Entity onEntitySpawnLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.NATURAL);
		return entity;
	}
}
