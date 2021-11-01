package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin
{
	@ModifyArg(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			allow = 1
	)
	private Entity onZombieReinforceSpawnedLifeTimeTracker(Entity zombieEntity)
	{
		((LifetimeTrackerTarget)zombieEntity).recordSpawning(LiteralSpawningReason.ZOMBIE_REINFORCE);
		return zombieEntity;
	}
}
