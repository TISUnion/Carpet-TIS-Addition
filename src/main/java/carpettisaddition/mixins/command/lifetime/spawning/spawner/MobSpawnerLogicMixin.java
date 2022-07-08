package carpettisaddition.mixins.command.lifetime.spawning.spawner;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11600
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin
{
	//#if MC >= 11600
	//$$ private Entity spawnedEntity$lifeTimeTracker;
	//#endif

	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "update",
			//#else
			method = "spawnEntity",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;shouldCreateNewEntityWithPassenger(Lnet/minecraft/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			),
			index = 0
	)
	//#if MC >= 11600
	//$$ private Entity recordSpawnedEntityLifeTimeTracker(Entity entity)
	//$$ {
	//$$ 	this.spawnedEntity$lifeTimeTracker = entity;
	//$$ 	return entity;
	//$$ }
	//#else
	private Entity onSpawnerLogicSpawnEntityLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SPAWNER);
		return entity;
	}
	//#endif

	//#if MC >= 11600
	//$$ @Inject(
	//$$ 		method = "update",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
	//$$ 		)
	//$$ )
	//$$ private void onSpawnerLogicSpawnEntityLifeTimeTracker(CallbackInfo ci)
	//$$ {
	//$$ 	if (this.spawnedEntity$lifeTimeTracker != null)
	//$$ 	{
	//$$ 		((LifetimeTrackerTarget)this.spawnedEntity$lifeTimeTracker).recordSpawning(LiteralSpawningReason.SPAWNER);
	//$$ 	}
	//$$ }
	//#endif
}
