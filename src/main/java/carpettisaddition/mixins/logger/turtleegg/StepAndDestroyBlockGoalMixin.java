package carpettisaddition.mixins.logger.turtleegg;

import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StepAndDestroyBlockGoal.class)
public abstract class StepAndDestroyBlockGoalMixin
{
	@Shadow @Final private MobEntity stepAndDestroyMob;

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void dontBreakTheEgg(CallbackInfo ci, World world, BlockPos blockPos, BlockPos blockPos2)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			BlockState blockState = world.getBlockState(blockPos2);
			if (blockState.getBlock() == Blocks.TURTLE_EGG)
			{
				TurtleEggLogger.getInstance().onBreakingEgg(world, blockPos2, blockState, this.stepAndDestroyMob);
			}
		}
	}
}
