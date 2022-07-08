package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public abstract class SummonVexGoalMixin
{
	@ModifyArg(
			method = "castSpell",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
					//#else
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			)
	)
	private Entity onVexSummonedLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.SUMMON);
		return entity;
	}
}
