package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void onUseOnBlock$MicroTimingLoggerMarker(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
	{
		if (MicroTimingLoggerManager.getInstance() != null && !world.isClient())
		{
			boolean accepted = MicroTimingLoggerManager.getInstance().onPlayerRightClick(player, hand, hit.getBlockPos());
			if (accepted)
			{
				cir.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}
