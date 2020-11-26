package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	// fabric-carpet used @Redirect so there goes @ModifyArg here xd
	// dont try to capture locals, WAY too many
	@ModifyArg(
			method = "spawnEntitiesInChunk",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0,
			allow = 1
	)
	private static Entity onEntitySpawnLifeTimeTracker(Entity entity)
	{
		((IEntity)entity).recordSpawning(LiteralSpawningReason.NATURAL);
		return entity;
	}
}
