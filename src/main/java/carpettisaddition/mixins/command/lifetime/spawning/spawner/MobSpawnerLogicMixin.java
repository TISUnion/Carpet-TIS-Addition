package carpettisaddition.mixins.command.lifetime.spawning.spawner;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * 1.16+ mixin is handled in the version-specified class
 */
@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin
{
	@ModifyArg(
			method = "spawnEntity",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity onSpawnerLogicSpawnEntityLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SPAWNER);
		return entity;
	}
}
