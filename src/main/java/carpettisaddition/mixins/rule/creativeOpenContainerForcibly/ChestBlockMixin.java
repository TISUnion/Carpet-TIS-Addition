package carpettisaddition.mixins.rule.creativeOpenContainerForcibly;

import carpettisaddition.helpers.rule.creativeOpenContainerForcibly.CreativeOpenContainerForciblyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin
{
	private static final ThreadLocal<Boolean> ignoreChestBlockedCheck = ThreadLocal.withInitial(() -> false);

	@Inject(
			method = "activate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/ChestBlock;createContainerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/container/NameableContainerFactory;"
			)
	)
	private void noCollideOrCreative(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
	{
		ignoreChestBlockedCheck.set(CreativeOpenContainerForciblyHelper.canOpenForcibly(player));
	}

	@Inject(
			method = "activate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/ChestBlock;createContainerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/container/NameableContainerFactory;",
					shift = At.Shift.AFTER
			)
	)
	private void noCollideOrCreativeReset(CallbackInfoReturnable<ActionResult> cir)
	{
		ignoreChestBlockedCheck.set(false);
	}

	// never try to target ChestBlock#getBlockEntitySource, yarn will not be happy with that at server-side runtime, no intermediary warning
	// see #17
	@Inject(method = "isChestBlocked", at = @At("HEAD"), cancellable = true)
	private static void believeMeTheChestIsNotBlocked(CallbackInfoReturnable<Boolean> cir)
	{
		if (ignoreChestBlockedCheck.get())
		{
			cir.setReturnValue(false);
		}
	}
}
