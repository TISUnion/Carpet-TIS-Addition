package carpettisaddition.mixins.rule.dispenserNoItemCost;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemDispenserBehavior.class)
public abstract class ItemDispenserBehaviorMixin
{
	@Shadow
	protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);

	@Redirect(
			method = "dispense",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
			)
	)
	private ItemStack injectNoCostBehavior(ItemDispenserBehavior itemDispenserBehavior, BlockPointer pointer, ItemStack stack)
	{
		if (CarpetTISAdditionSettings.dispenserNoItemCost)
		{
			this.dispenseSilently(pointer, stack.copy());
			return stack;
		}
		else
		{
			return this.dispenseSilently(pointer, stack);
		}
	}
}
