package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.entity.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin
{
	@Inject(
			method = "checkDespawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/boss/WitherEntity;remove()V"
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_DIFFICULTY);
	}
}
