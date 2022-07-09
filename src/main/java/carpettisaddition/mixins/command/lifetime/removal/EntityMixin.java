package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.DamageableEntity;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.DeathRemovalReason;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.TransDimensionRemovalReason;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11600
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import org.jetbrains.annotations.Nullable;
//#else
import net.minecraft.world.dimension.DimensionType;
//#endif

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
			//#if MC >= 11600
			//$$ method = "moveToWorld",
			//#else
			method = "changeDimension",
			//#endif
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11500
							target = "Lnet/minecraft/server/world/ServerWorld;onDimensionChanged(Lnet/minecraft/entity/Entity;)V"
							//#else
							//$$ target = "Lnet/minecraft/server/world/ServerWorld;method_18769(Lnet/minecraft/entity/Entity;)V"
							//#endif
					)
			),
			at = @At(
					//#if MC >= 11600
					//$$ value = "INVOKE",
					//$$ //#if MC >= 11700
					//$$ //$$ target = "Lnet/minecraft/entity/Entity;removeFromDimension()V"
					//$$ //#else
					//$$ target = "Lnet/minecraft/entity/Entity;method_30076()V"
					//$$ //#endif
					//#else
					value = "FIELD",
					target = "Lnet/minecraft/entity/Entity;removed:Z"
					//#endif
			),
			allow = 1
	)
	private void onEntityTransDimensionRemovedLifeTimeTracker(
			//#if MC >= 11600
			//$$ ServerWorld destination, CallbackInfoReturnable<@Nullable Entity> cir
			//#else
			DimensionType destination, CallbackInfoReturnable<Entity> cir
			//#endif
	)
	{
		((LifetimeTrackerTarget)this).recordRemoval(new TransDimensionRemovalReason(DimensionWrapper.of(destination)));
	}

	//#if MC >= 11600
	//$$ @Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("RETURN"))
	//$$ private void onEntityStartRidingLifeTimeTracker(CallbackInfoReturnable<Boolean> cir)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap)
	//$$ 	{
	//$$ 		if (this.hasVehicle())
	//$$ 		{
	//$$ 			((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.ON_VEHICLE);
	//$$ 		}
	//$$ 	}
	//$$ }
	//#endif

	@Inject(
			//#if MC >= 11700
			//$$ method = "tickInVoid",
			//#else
			method = "destroy",
			//#endif
			at = @At("HEAD")
	)
	private void onEntityDestroyedInVoid(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.VOID);
	}
}
