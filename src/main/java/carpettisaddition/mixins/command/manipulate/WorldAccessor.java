package carpettisaddition.mixins.command.manipulate;

import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(World.class)
public interface WorldAccessor
{
	@Accessor
	boolean isIteratingTickingBlockEntities();

	@Accessor
	List<BlockEntityTickInvoker> getBlockEntityTickers();
}
