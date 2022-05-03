package carpettisaddition.mixins.utils;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$DirectBlockEntityTickInvoker")
public interface DirectBlockEntityTickInvokerAccessor<T extends BlockEntity>
{
	@Accessor
	T getBlockEntity();
}
