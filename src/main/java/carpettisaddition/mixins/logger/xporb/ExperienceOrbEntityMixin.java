package carpettisaddition.mixins.logger.xporb;

import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	private boolean flagDied = false;
	private boolean flagDespawned = false;

	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDI)V", at = @At("TAIL"))
	private void onCreated(CallbackInfo ci)
	{
		XPOrbLogger.getInstance().onEntityCreated((ExperienceOrbEntity)(Object)this);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;discard()V"
			)
	)
	void onDespawned(CallbackInfo ci)
	{
		if (!this.flagDespawned)
		{
			XPOrbLogger.getInstance().onEntityDespawn((ExperienceOrbEntity)(Object)this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;discard()V"
			)
	)
	void onDied(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (!this.flagDied)
		{
			XPOrbLogger.getInstance().onEntityDied((ExperienceOrbEntity)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
