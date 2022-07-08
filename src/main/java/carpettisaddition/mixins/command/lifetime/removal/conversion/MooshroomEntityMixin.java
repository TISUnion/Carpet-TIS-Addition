package carpettisaddition.mixins.command.lifetime.removal.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.MobConversionRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC >= 11600
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin extends Entity
{
	public MooshroomEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "sheared",
			//#else
			method = "interactMob",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/passive/MooshroomEntity;remove()V",
					ordinal = 0
			)
	)
	private void recordSelfRemoval$LifeTimeTracker(
			//#if MC >= 11600
			//$$ CallbackInfo ci
			//#else
			CallbackInfoReturnable<Boolean> cir
			//#endif
	)
	{
		((LifetimeTrackerTarget)this).recordRemoval(new MobConversionRemovalReason(EntityType.COW));
	}
}
