package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
//$$ import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
//$$ import net.minecraft.block.BarrelBlock;
//$$ import net.minecraft.block.Block;
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.inventory.Inventory;
//$$ import net.minecraft.util.math.BlockPos;
//$$ import net.minecraft.world.World;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"),
		@Condition(ModIds.lithium)
})
@Mixin(
		//#if MC >= 11700
		//$$ HopperHelper.class
		//#else
		DummyClass.class
		//#endif
)
public abstract class HopperHelperMixin
{
	//#if MC >= 11700
	//$$ @Inject(
	//$$ 		method = "vanillaGetBlockInventory",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;",
	//$$ 				remap = true
	//$$ 		),
	//$$ 		locals = LocalCapture.CAPTURE_FAILHARD,
	//$$ 		cancellable = true,
	//$$ 		remap = false
	//$$ )
	//$$ private static void useLargeBarrelInventoryMaybe(World world, BlockPos blockPos, CallbackInfoReturnable<Inventory> cir, Inventory inventory, BlockState blockState, Block block)
	//$$ {
	//$$ 	// note: inventory is always null
	//$$ 	if (CarpetTISAdditionSettings.largeBarrel)
	//$$ 	{
	//$$ 		if (block instanceof BarrelBlock)
	//$$ 		{
	//$$ 			Inventory largeBarrel = LargeBarrelHelper.getInventory(blockState, world, blockPos);
	//$$ 			if (largeBarrel != null)
	//$$ 			{
	//$$ 				cir.setReturnValue(largeBarrel);
	//$$ 			}
	//$$ 		}
	//$$ 	}
	//$$ }
	//#endif
}