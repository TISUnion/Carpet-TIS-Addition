package carpettisaddition.mixins.rule.creativeNetherWaterPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//#if MC >= 11600
//$$ import net.minecraft.world.dimension.DimensionType;
//#else
import net.minecraft.world.dimension.Dimension;
//#endif

@Mixin(BucketItem.class)
public abstract class BucketItemMixin
{
	@Redirect(
			method = "placeFluid",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/dimension/DimensionType;isUltrawarm()Z"
					//#else
					target = "Lnet/minecraft/world/dimension/Dimension;doesWaterVaporize()Z"
					//#endif
			),
			require = 0
	)
	private boolean creativeNetherWaterPlacement(
			//#if MC >= 11600
			//$$ DimensionType dimension,
			//#else
			Dimension dimension,
			//#endif
			/* parent method parameters v */
			@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult
	)
	{
		if (CarpetTISAdditionSettings.creativeNetherWaterPlacement)
		{
			if (player != null && player.isCreative())
			{
				return false;
			}
		}
		// vanilla
		//#if MC >= 11600
		//$$ return dimension.isUltrawarm();
		//#else
		return dimension.doesWaterVaporize();
		//#endif
	}
}
