package carpettisaddition.mixins.rule.hopperNoItemCost.compact.lithium;

import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HopperHelper.class)
public abstract class HopperHelperMixin
{
	@ModifyArg(
			method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
			)
	)
	private static int hopperNoItemCost(int value)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			HopperBlockEntity hopper = HopperNoItemCostHelper.currentHopper.get();
			if (hopper != null && hopper.getWorld() != null)
			{
				DyeColor wool_color = WoolTool.getWoolColorAtPosition(hopper.getWorld(), hopper.getPos().offset(Direction.UP));
				if (wool_color != null)
				{
					// no item cost!
					return 0;
				}
			}
		}
		return value;
	}
}
