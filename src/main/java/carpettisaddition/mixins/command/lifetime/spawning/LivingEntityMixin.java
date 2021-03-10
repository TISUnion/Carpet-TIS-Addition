package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	// wither rose thing
	@ModifyArg(
			method = "onDeath",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private Entity onMobDroppedItemLifeTimeTracker(Entity itemEntity)
	{
		((IEntity)itemEntity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return itemEntity;
	}

	@ModifyArg(
			method = "updatePostDeath",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity onMobDroppedXpLifeTimeTracker(Entity entity)
	{
		((IEntity)entity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return entity;
	}
}
