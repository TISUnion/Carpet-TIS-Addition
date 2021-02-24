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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin
{
	private final ThreadLocal<Boolean> ignoreBlockedForCreative = ThreadLocal.withInitial(() -> false);

	@Inject(
			method = "activate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/ChestBlock;createContainerFactory(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/container/NameableContainerFactory;"
			)
	)
	private void noCollideOrCreative(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
	{
		if (CreativeOpenContainerForciblyHelper.canOpenForcibly(player))
		{
			this.ignoreBlockedForCreative.set(true);
		}
	}

	@ModifyArg(
			method = "createContainerFactory",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/ChestBlock;retrieve(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/block/ChestBlock$PropertyRetriever;)Ljava/lang/Object;"
			),
			index = 3
	)
	private boolean modifyShouldIgnoreBlockParameter(boolean value)
	{
		if (this.ignoreBlockedForCreative.get())
		{
			this.ignoreBlockedForCreative.set(false);
			return true;
		}
		return value;
	}
}
