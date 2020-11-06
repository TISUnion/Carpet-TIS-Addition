package carpettisaddition.mixins.logger.microtiming.tickstages;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldChunk.class_5563.class)
public interface class_5563Accessor<T extends BlockEntity>
{
	@Accessor("field_27224")
	T getBlockEntity();
}
