package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin
{
	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
			),
			allow = 1,
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onZombieReinforceSpawnedLifeTimeTracker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, ServerWorld serverWorld, LivingEntity livingEntity, int i, int j, int k, ZombieEntity zombieEntity)
	{
		((IEntity)zombieEntity).recordSpawning(LiteralSpawningReason.ZOMBIE_REINFORCE);
	}
}
