package carpettisaddition.mixins.rule.creativeOpenContainerForcibly;

import carpettisaddition.helpers.rule.creativeOpenContainerForcibly.CreativeOpenContainerForciblyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin
{
	@Shadow
	private static boolean method_33383(BlockState blockState, World world, BlockPos blockPos, ShulkerBoxBlockEntity shulkerBoxBlockEntity)
	{
		return false;
	}

	@Redirect(
			method = "onUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/ShulkerBoxBlock;method_33383(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;)Z"
			)
	)
	private boolean noCollideOrCreative(BlockState state, World world, BlockPos pos, ShulkerBoxBlockEntity shulkerBoxBlockEntity, /* parent method parameters -> */ BlockState state2, World world2, BlockPos pos2, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (CreativeOpenContainerForciblyHelper.canOpenForcibly(player))
		{
			return true;
		}
		// vanilla
		return method_33383(state, world, pos, shulkerBoxBlockEntity);
	}
}
