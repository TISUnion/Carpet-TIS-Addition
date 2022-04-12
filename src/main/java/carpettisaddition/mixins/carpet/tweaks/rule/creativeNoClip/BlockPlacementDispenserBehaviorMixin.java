package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.compat.CarpetSettings;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPlacementDispenserBehavior.class)
public abstract class BlockPlacementDispenserBehaviorMixin
{
	@Inject(method = "dispenseSilently", at = @At("HEAD"))
	private void creativeNoClipEnhancementEnter(CallbackInfoReturnable<ItemStack> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(true);
		}
	}

	@Inject(method = "dispenseSilently", at = @At("TAIL"))
	private void creativeNoClipEnhancementExit(CallbackInfoReturnable<ItemStack> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(false);
		}
	}
}
