package carpettisaddition.mixins.command.lifetime.spawning.breeding;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin
{
	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "randomTick",
			//#elseif MC >= 11500
			method = "scheduledTick",
			//#else
			//$$ method = "onScheduledTick",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#else
					//$$ target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			)
	)
	private Entity onAnimalBirthLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.BREEDING);
		return entity;
	}
}
