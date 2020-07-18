package carpettisaddition.mixins.option;

import carpettisaddition.helpers.DispenserHelper;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(DispenserBlock.class)
public abstract class DispenserBlock_dispensersFireDragonBreathMixin
{
    @Inject(
            method = "getBehaviorForItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void registerCarpetBehaviors(ItemStack stack, CallbackInfoReturnable<DispenserBehavior> cir)
    {
        Item item = stack.getItem();
        if (item == Items.DRAGON_BREATH)
        {
            cir.setReturnValue(new DispenserHelper.FireDragonBreathDispenserBehaviour());
        }
    }
}
