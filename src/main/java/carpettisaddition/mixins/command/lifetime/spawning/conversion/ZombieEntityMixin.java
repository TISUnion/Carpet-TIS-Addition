package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity
{
	protected ZombieEntityMixin(EntityType<? extends HostileEntity> type, World world)
	{
		super(type, world);
	}

	@ModifyArg(
			method = "convertTo",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)

	private Entity recordZombieVariantSpawning$LifeTimeTracker(Entity zombieVariant)
	{
		((LifetimeTrackerTarget)zombieVariant).recordSpawning(new MobConversionSpawningReason(this.getType()));
		return zombieVariant;
	}

	@Inject(
			method = "onKilledOther",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void recordZombieVillagerSpawning$LifeTimeTracker(LivingEntity other, CallbackInfo ci, VillagerEntity villagerEntity, ZombieVillagerEntity zombieVillagerEntity)
	{
		((LifetimeTrackerTarget)zombieVillagerEntity).recordSpawning(new MobConversionSpawningReason(villagerEntity.getType()));
	}
}
