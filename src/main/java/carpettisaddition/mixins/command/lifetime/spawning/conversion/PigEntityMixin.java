package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity
{
	protected PigEntityMixin(EntityType<? extends AnimalEntity> type, World world)
	{
		super(type, world);
	}

	@ModifyArg(
			method = "onStruckByLightning",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#endif
			)
	)
	private Entity recordZombiePigmanSpawning$LifeTimeTracker(Entity zombiePigman)
	{
		((LifetimeTrackerTarget)zombiePigman).recordSpawning(new MobConversionSpawningReason(this.getType()));
		return zombiePigman;
	}
}
