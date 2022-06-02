package carpettisaddition.mixins.command.lifetime.spawning.natural;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PatrolSpawner.class)
public abstract class PillagerSpawnerMixin
{
	@ModifyArg(
			method = "spawnPillager",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
			)
	)
	private Entity onPillagerSpawnLifeTimeTracker(Entity pillager)
	{
		((LifetimeTrackerTarget)pillager).recordSpawning(LiteralSpawningReason.NATURAL);
		return pillager;
	}
}
