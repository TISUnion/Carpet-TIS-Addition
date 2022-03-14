package carpettisaddition.mixins.command.lifetime.spawning.item;

import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import carpettisaddition.commands.lifetime.utils.ExperienceOrbEntityUtil;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBottleEntity.class)
public abstract class ThrownExperienceBottleEntityMixin
{
	@Inject(
			method = "onCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"
			)
	)
	private void onXPBottleDroppedXpLifeTimeTracker(CallbackInfo ci)
	{
		ExperienceOrbEntityUtil.spawningReason.set(LiteralSpawningReason.ITEM);
	}
}
