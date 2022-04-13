package carpettisaddition.mixins.rule.hopperNoItemCost.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HopperHelper.class)
public abstract class HopperHelperMixin
{
	@ModifyVariable(
			method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z",
			at = @At(value = "HEAD"),
			argsOnly = true,
			index = 2
	)
	private static ItemStack hopperNoItemCost(ItemStack transferStack)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost && HopperNoItemCostHelper.enable.get())
		{
			transferStack = transferStack.copy();
		}
		return transferStack;
	}
}
