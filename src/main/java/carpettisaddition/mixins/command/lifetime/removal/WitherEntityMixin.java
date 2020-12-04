package carpettisaddition.mixins.command.lifetime.removal;

import net.minecraft.entity.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin
{
	// does not exists in 1.14
//	@Inject(
//			method = "checkDespawn",
//			at = @At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/entity/boss/WitherEntity;remove()V"
//			)
//	)
//	private void onDespawnLifeTimeTracker(CallbackInfo ci)
//	{
//		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_DIFFICULTY);
//	}
}
