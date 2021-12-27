package carpettisaddition.mixins.command.lifetime.spawning.breeding;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.entity.passive.FoxEntity$MateGoal")
public abstract class FoxEntityMateGoalMixin
{
	@ModifyArg(
			method = "breed",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
					ordinal = 0
			)
	)
	private Entity onAnimalBirthLifeTimeTracker(Entity entity)
	{
		if (entity != null)
		{
			((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.BREEDING);
		}
		return entity;
	}
}
