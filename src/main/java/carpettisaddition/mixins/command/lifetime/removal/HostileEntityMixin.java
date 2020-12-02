package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin
{
	// peaceful despawn thing
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/HostileEntity;remove()V"
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN);
	}
}
