package carpettisaddition.mixins.logger.microtiming;

import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface AbstractBlockAccessor
{
	@Accessor("DIRECTIONS")
	static Direction[] getFACINGS()
	{
		throw new RuntimeException();
	}
}
