package carpettisaddition.mixins.logger.microtiming;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor
{
	@Accessor
	static Direction[] getFACINGS()
	{
		return null;
	}
}
