package carpettisaddition.mixins.option;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemDispenserBehavior.class)
public abstract class ItemDispenserBehavior_noCostMixin {
    @Redirect(method = "dispense", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack injectNoCostBehavior(ItemDispenserBehavior itemDispenserBehavior, BlockPointer pointer, ItemStack stack) {
        if (CarpetTISAdditionSettings.dispenserNoItemCost) {
            ItemStack copied = stack.copy();
            this.dispenseSilently(pointer, copied);
        } else {
            this.dispenseSilently(pointer, stack);
        }
        return stack;
    }

    @Shadow
    protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);
}
