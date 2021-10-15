package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
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
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
			),
			allow = 1
	)
	private Entity onZombieReinforceSpawnedLifeTimeTracker(Entity zombieEntity)
	{
		((IEntity)zombieEntity).recordSpawning(LiteralSpawningReason.ZOMBIE_REINFORCE);
		return zombieEntity;
	}
}
