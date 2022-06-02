package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin
{
	@Inject(method = "setCarriedBlock", at = @At("TAIL"))
	private void noMoreCountingTowardsMobcapLifetimeTracker(BlockState state, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap)
		{
			if (state != null)
			{
				((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.PICKUP_BLOCK);
			}
		}
	}
}
