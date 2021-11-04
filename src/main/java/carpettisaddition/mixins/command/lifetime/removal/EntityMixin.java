package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.DamageableEntity;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.DeathRemovalReason;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.TransDimensionRemovalReason;
import carpettisaddition.utils.DimensionWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public abstract boolean hasVehicle();

	@Inject(method = "remove", at = @At("TAIL"))
	private void onEntityRemovedLifeTimeTracker(CallbackInfo ci)
	{
		if (this instanceof DamageableEntity)
		{
			DamageSource damageSource = ((DamageableEntity)this).getDeathDamageSource();
			if (damageSource != null)
			{
				((LifetimeTrackerTarget)this).recordRemoval(new DeathRemovalReason(damageSource));
				return;
			}
		}
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.OTHER);
	}

	@Inject(
			method = "moveToWorld",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/world/ServerWorld;onDimensionChanged(Lnet/minecraft/entity/Entity;)V"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;method_30076()V"
			),
			allow = 1
	)
	private void onEntityTransDimensionRemovedLifeTimeTracker(ServerWorld destination, CallbackInfoReturnable<@Nullable Entity> cir)
	{
		((IEntity)this).recordRemoval(new TransDimensionRemovalReason(destination.getRegistryKey()));
	}

	@Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("RETURN"))
	void onEntityStartRidingLifeTimeTracker(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.hasVehicle() && )
		{
			((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.ON_VEHICLE);
		}
	}

	@Inject(method = "destroy", at = @At("HEAD"))
	private void onEntityDestroyedInVoid(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.VOID);
	}
}
