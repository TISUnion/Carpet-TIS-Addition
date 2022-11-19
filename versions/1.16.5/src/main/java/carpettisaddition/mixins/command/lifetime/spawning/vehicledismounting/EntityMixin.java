package carpettisaddition.mixins.command.lifetime.spawning.vehicledismounting;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Inject(
			//#if MC >= 11700
			//$$ method = "dismountVehicle",
			//#else
			method = "method_29239",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;removePassenger(Lnet/minecraft/entity/Entity;)V",
					shift = At.Shift.AFTER
			)
	)
	private void onEntityStopRidingLifeTimeTracker(CallbackInfo ci)
	{
		// for living entity, the logic is handled in LivingEntityMixin
		// so the correct dismounting pos can be recorded
		//noinspection ConstantConditions
		if (!((Entity)(Object)this instanceof LivingEntity))
		{
			((LifetimeTrackerTarget)this).recordSpawning(LiteralSpawningReason.VEHICLE_DISMOUNTING);
		}
	}
}
