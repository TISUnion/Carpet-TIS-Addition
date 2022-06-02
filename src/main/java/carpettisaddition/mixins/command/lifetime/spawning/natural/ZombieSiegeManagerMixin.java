package carpettisaddition.mixins.command.lifetime.spawning.natural;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.village.ZombieSiegeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieSiegeManager.class)
public abstract class ZombieSiegeManagerMixin
{
	@ModifyArg(
			method = "trySpawnZombie",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
			)
	)
	private Entity onZombieSpawnInZombieSiegeLifeTimeTracker(Entity zombie)
	{
		((LifetimeTrackerTarget)zombie).recordSpawning(LiteralSpawningReason.NATURAL);
		return zombie;
	}
}
