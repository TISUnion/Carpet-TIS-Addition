package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11500
import net.minecraft.util.ActionResult;
//#endif

@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@Inject(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void onUseOnBlock$MicroTimingLoggerMarker(
			World world, PlayerEntity player, Hand hand, BlockHitResult hit,
			//#if MC >= 11500
			CallbackInfoReturnable<ActionResult> cir
			//#else
			//$$ CallbackInfoReturnable<Boolean> cir
			//#endif
	)
	{
		// client side is allowed here so the client wont desync
		boolean accepted = MicroTimingLoggerManager.onPlayerRightClick(player, hand, hit.getBlockPos());
		if (accepted)
		{
			cir.setReturnValue(
					//#if MC >= 11500
					ActionResult.CONSUME
					//#else
					//$$ true
					//#endif
			);
		}
	}
}
