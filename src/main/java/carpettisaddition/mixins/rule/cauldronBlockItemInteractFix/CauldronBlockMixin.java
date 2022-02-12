package carpettisaddition.mixins.rule.cauldronBlockItemInteractFix;

import net.minecraft.block.CauldronBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin
{
	// no more cauldronBlockItemInteractFix in 1.17+
}
