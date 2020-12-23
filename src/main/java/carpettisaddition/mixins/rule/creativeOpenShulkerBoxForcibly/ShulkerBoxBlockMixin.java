package carpettisaddition.mixins.rule.creativeOpenShulkerBoxForcibly;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin
{
	@Redirect(
			method = "onUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;doesNotCollide(Lnet/minecraft/util/math/Box;)Z"
			)
	)
	private boolean noCollideOrCreative(World world, Box box, /* parent method parameters -> */ BlockState state, World world2, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (CarpetTISAdditionSettings.creativeOpenShulkerBoxForcibly)
		{
			if (player.isCreative())
			{
				return true;
			}
		}
		// vanilla
		return world.doesNotCollide(box);
	}
}
