package carpettisaddition.mixins.command.lifetime.spawning.vehicledismounting;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@Inject(
			method = "stopRiding",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;onDismounted(Lnet/minecraft/entity/Entity;)V",
					shift = At.Shift.AFTER
			)
	)
	private void onLivingEntityStopRidingLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordSpawning(LiteralSpawningReason.VEHICLE_DISMOUNTING);
	}
}
