package carpettisaddition.mixins.command.lifetime.spawning.spawner;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.15- mixin is handled in the version-specified class
 */
@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin
{
	private Entity spawnedEntity$lifeTimeTracker;

	@ModifyArg(
			//#if MC >= 11700
			//$$ method = "serverTick",
			//#else
			method = "update",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/server/world/ServerWorld;shouldCreateNewEntityWithPassenger(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			),
			index = 0
	)
	private Entity recordSpawnedEntityLifeTimeTracker(Entity entity)
	{
		this.spawnedEntity$lifeTimeTracker = entity;
		return entity;
	}

	@Inject(
			//#if MC >= 11700
			//$$ method = "serverTick",
			//#else
			method = "update",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#else
					target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#endif
			)
	)
	private void onSpawnerLogicSpawnEntityLifeTimeTracker(CallbackInfo ci)
	{
		if (this.spawnedEntity$lifeTimeTracker != null)
		{
			((LifetimeTrackerTarget)this.spawnedEntity$lifeTimeTracker).recordSpawning(LiteralSpawningReason.SPAWNER);
		}
	}
}
