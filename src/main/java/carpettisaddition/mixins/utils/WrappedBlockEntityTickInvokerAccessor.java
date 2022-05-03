package carpettisaddition.mixins.utils;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$WrappedBlockEntityTickInvoker")
public interface WrappedBlockEntityTickInvokerAccessor<T extends BlockEntity>
{
	@Accessor
	BlockEntityTickInvoker getWrapped();
}
