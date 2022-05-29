package carpettisaddition.mixins.command.lifetime.spawning.natural;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	@ModifyArg(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private Entity onPhantomSpawnLifeTimeTracker(Entity phantom)
	{
		((LifetimeTrackerTarget)phantom).recordSpawning(LiteralSpawningReason.NATURAL);
		return phantom;
	}
}
