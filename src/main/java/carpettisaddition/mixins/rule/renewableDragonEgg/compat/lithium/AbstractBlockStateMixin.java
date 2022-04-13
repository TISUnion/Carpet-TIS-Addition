package carpettisaddition.mixins.rule.renewableDragonEgg.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(ModIds.lithium))
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin
{
	@Shadow public abstract Block getBlock();

	/**
	 * Lithium block.flatten_states sets up immutable hasRandomTicks cache, and the cache may not match the actual value
	 * since whether dragon egg block has random tick is changeable
	 * So, if the block is dragon egg, use our own carpet setting rule value
	 */
	@Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
	private void hasRandomTicksDragonEgg(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.getBlock() == Blocks.DRAGON_EGG)
		{
			cir.setReturnValue(CarpetTISAdditionSettings.renewableDragonEgg);
			cir.cancel();
		}
	}
}
