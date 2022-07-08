package carpettisaddition.mixins.command.lifetime.spawning.natural;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.gen.PillagerSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PillagerSpawner.class)
public abstract class PillagerSpawnerMixin
{
	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "spawnPillager",
			//#else
			method = "spawnOneEntity",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
					//#else
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			)
	)
	private Entity onPillagerSpawnLifeTimeTracker(Entity pillager)
	{
		((LifetimeTrackerTarget)pillager).recordSpawning(LiteralSpawningReason.NATURAL);
		return pillager;
	}
}
