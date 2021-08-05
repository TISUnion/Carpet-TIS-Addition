package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpettisaddition.helpers.carpet.tweaks.logger.projectile.ProjectileLoggerTarget;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.TrajectoryLoggerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// smaller priority to make this execute before carpet's logger creation
@Mixin(value = ProjectileEntity.class, priority = 500)
public abstract class ProjectileEntityMixin implements ProjectileLoggerTarget
{
	@Unique
	private HitResult hitResult;

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
	private void recordEntityInfoForCarpetTrajectoryLogHelper(CallbackInfo ci)
	{
		TrajectoryLoggerUtil.currentEntity.set((Entity)(Object)this);
	}

	@Inject(method = "onCollision", at = @At("HEAD"))
	private void recordHitPoint(HitResult hitResult, CallbackInfo ci)
	{
		this.hitResult = hitResult;
	}

	@Override
	public HitResult getHitResult()
	{
		return this.hitResult;
	}
}
