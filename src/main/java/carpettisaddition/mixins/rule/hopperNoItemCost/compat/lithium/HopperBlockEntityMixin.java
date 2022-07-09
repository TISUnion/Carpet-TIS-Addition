package carpettisaddition.mixins.rule.hopperNoItemCost.compat.lithium;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import carpet.utils.WoolTool;
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
//$$ import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.util.DyeColor;
//$$ import net.minecraft.util.math.BlockPos;
//$$ import net.minecraft.util.math.Direction;
//$$ import net.minecraft.world.World;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import java.util.function.BooleanSupplier;
//#endif

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"),
		@Condition(ModIds.lithium)
})
@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	//#if MC >= 11700
	//$$ @Inject(
	//$$ 		method = "insertAndExtract",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/block/entity/HopperBlockEntity;insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/inventory/Inventory;)Z"
	//$$ 		)
	//$$ )
	//$$ private static void TISCMLithiumCompact$beforeInsert(World world, BlockPos pos, BlockState state, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.hopperNoItemCost)
	//$$ 	{
	//$$ 		DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, pos.offset(Direction.UP));
	//$$ 		if (wool_color != null)
	//$$ 		{
	//$$ 			if (hopperBlockEntity instanceof LithiumInventory)
	//$$ 			{
	//$$ 				HopperNoItemCostHelper.woolColor.set(wool_color);
	//$$ 			}
	//$$ 		}
	//$$ 	}
	//$$ }
 //$$
	//$$ @Inject(
	//$$ 		method = "insertAndExtract",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/block/entity/HopperBlockEntity;insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/inventory/Inventory;)Z",
	//$$ 				shift = At.Shift.AFTER
	//$$ 		)
	//$$ )
	//$$ private static void TISCMLithiumCompact$afterInsert(World world, BlockPos pos, BlockState state, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.hopperNoItemCost)
	//$$ 	{
	//$$ 		HopperNoItemCostHelper.woolColor.set(null);
	//$$ 	}
	//$$ }
	//#endif
}