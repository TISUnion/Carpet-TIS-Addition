package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import carpettisaddition.commands.lifetime.utils.ExperienceOrbEntityFlags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyArg(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0,
			allow = 1
	)
	private static Entity onXpOrbSpawnLifeTimeTracker(Entity entity)
	{
		if (ExperienceOrbEntityFlags.recordSpawning)
		{
			((IEntity)entity).recordSpawning(new MobDropSpawningReason(entity.getType()));
		}
		return entity;
	}
}
