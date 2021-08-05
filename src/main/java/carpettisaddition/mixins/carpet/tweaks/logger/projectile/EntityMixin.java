package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpettisaddition.helpers.carpet.tweaks.logger.projectile.VisualizeTrajectoryHelper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void dontMoveVisualizeProjectile(CallbackInfo ci)
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile((Entity)(Object)this))
		{
			ci.cancel();
		}
	}
}
