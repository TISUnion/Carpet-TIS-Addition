package carpettisaddition.mixins.rule.hopperNoItemCost.compat.lithium;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import carpettisaddition.commands.scounter.SupplierCounterCommand;
//$$ import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
//$$ import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
//$$ import net.minecraft.item.ItemStack;
//$$ import net.minecraft.util.DyeColor;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
	//$$ private static ItemStack originalTransferStack$TISCM = null;
	//$$
	//$$ @ModifyVariable(
	//$$ 		method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z",
	//$$ 		at = @At(value = "HEAD"),
	//$$ 		argsOnly = true,
	//$$ 		index = 2
	//$$ )
	//$$ private static ItemStack hopperNoItemCost(ItemStack transferStack)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.hopperNoItemCost && HopperNoItemCostHelper.woolColor.get() != null)
	//$$ 	{
	//$$ 		originalTransferStack$TISCM = transferStack;
	//$$ 		transferStack = transferStack.copy();
	//$$ 	}
	//$$ 	return transferStack;
	//$$ }
	//$$
	//$$ @ModifyVariable(
	//$$ 		method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z",
	//$$ 		at = @At(value = "RETURN"),
	//$$ 		argsOnly = true,
	//$$ 		index = 2
	//$$ )
	//$$ private static ItemStack hopperNoItemCost_supplierCounter(ItemStack transferStack)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.hopperNoItemCost)
	//$$ 	{
	//$$ 		DyeColor woolColor = HopperNoItemCostHelper.woolColor.get();
	//$$ 		if (woolColor != null && originalTransferStack$TISCM != null)
	//$$ 		{
	//$$ 			if (SupplierCounterCommand.isActivated())
	//$$ 			{
	//$$ 				SupplierCounterCommand.getInstance().record(woolColor, originalTransferStack$TISCM, transferStack);
	//$$ 			}
	//$$ 			originalTransferStack$TISCM = null;
	//$$ 		}
	//$$ 	}
	//$$ 	return transferStack;
	//$$ }
	//#endif
}