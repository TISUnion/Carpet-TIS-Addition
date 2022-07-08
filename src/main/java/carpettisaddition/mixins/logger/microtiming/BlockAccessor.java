package carpettisaddition.mixins.logger.microtiming;

import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11600
//$$import net.minecraft.block.AbstractBlock;
//#else
import net.minecraft.block.Block;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ AbstractBlock.class
		//#else
		Block.class
		//#endif
)
public interface BlockAccessor
{
	@Accessor
	static Direction[] getFACINGS()
	{
		throw new RuntimeException();
	}
}
