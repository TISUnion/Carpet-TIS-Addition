package carpettisaddition.mixins.rule.tntFuseDuration;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.TntEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin
{
	@Shadow public abstract void setFuse(int fuse);

	@Shadow private int fuseTimer;

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
	private void modifyFuseTimer1(CallbackInfo ci)
	{
		this.fuseTimer = CarpetTISAdditionSettings.tntFuseDuration;
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
	private void modifyFuseTimer2(CallbackInfo ci)
	{
		this.setFuse(CarpetTISAdditionSettings.tntFuseDuration);
	}

	@ModifyArg(
			method = "initDataTracker",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/data/DataTracker;startTracking(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V"
			),
			index = 1
	)
	private Object getCustomFuseTimer(Object oldValue)
	{
		return CarpetTISAdditionSettings.tntFuseDuration;
	}
}
