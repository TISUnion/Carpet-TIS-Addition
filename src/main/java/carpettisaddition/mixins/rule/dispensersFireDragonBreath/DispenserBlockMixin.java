package carpettisaddition.mixins.rule.dispensersFireDragonBreath;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.dispensersFireDragonBreath.FireDragonBreathDispenserBehaviour;
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
public abstract class DispenserBlockMixin
{
    @Inject(method = "getBehaviorForItem", at = @At("HEAD"), cancellable = true)
    private void registerCarpetBehaviors(ItemStack stack, CallbackInfoReturnable<DispenserBehavior> cir)
    {
        if (CarpetTISAdditionSettings.dispensersFireDragonBreath)
        {
            Item item = stack.getItem();
            if (item == Items.DRAGON_BREATH)
            {
                cir.setReturnValue(FireDragonBreathDispenserBehaviour.INSTANCE);
            }
        }
    }
}
