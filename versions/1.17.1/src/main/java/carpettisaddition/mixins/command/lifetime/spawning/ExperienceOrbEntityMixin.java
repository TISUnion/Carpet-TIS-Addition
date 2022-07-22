package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.utils.LifetimeMixinUtil;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyArg(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0,
			allow = 1
	)
	private static Entity onXpOrbSpawnLifeTimeTracker(Entity entity)
	{
		if (LifetimeMixinUtil.xpOrbSpawningReason.get() != null)
		{
			((LifetimeTrackerTarget)entity).recordSpawning(LifetimeMixinUtil.xpOrbSpawningReason.get());
		}
		return entity;
	}

	@Inject(method = "spawn", at = @At("TAIL"))
	private static void onXpOrbSpawnEndLifeTimeTracker(CallbackInfo ci)
	{
		LifetimeMixinUtil.xpOrbSpawningReason.remove();
	}
}