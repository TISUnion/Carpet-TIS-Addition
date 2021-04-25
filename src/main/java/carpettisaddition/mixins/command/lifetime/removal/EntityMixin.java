package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.EntityDamageable;
import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.DeathRemovalReason;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.TransDimensionRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Inject(method = "remove", at = @At("TAIL"))
	private void onEntityRemovedLifeTimeTracker(CallbackInfo ci)
	{
		if (this instanceof EntityDamageable)
		{
			DamageSource damageSource = ((EntityDamageable)this).getDeathDamageSource();
			if (damageSource != null)
			{
				((IEntity)this).recordRemoval(new DeathRemovalReason(damageSource));
				return;
			}
		}
		((IEntity)this).recordRemoval(LiteralRemovalReason.OTHER);
	}

	@Inject(
			method = "changeDimension",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/world/ServerWorld;onDimensionChanged(Lnet/minecraft/entity/Entity;)V"
					)
			),
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/Entity;removed:Z"
			),
			allow = 1
	)
	private void onEntityTransDimensionRemovedLifeTimeTracker(DimensionType newDimension, CallbackInfoReturnable<Entity> cir)
	{
		((IEntity)this).recordRemoval(new TransDimensionRemovalReason(newDimension));
	}

	@Inject(method = "destroy", at = @At("HEAD"))
	private void onEntityDestroyedInVoid(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.VOID);
	}
}
