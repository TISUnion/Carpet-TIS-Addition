package carpettisaddition.mixins.command.manipulate;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11700
//$$ import net.minecraft.world.chunk.BlockEntityTickInvoker;
//$$ import java.util.List;
//#endif

@Mixin(World.class)
public interface WorldAccessor
{
	@Accessor
	boolean isIteratingTickingBlockEntities();

	//#if MC >= 11700
	//$$ @Accessor
	//$$ List<BlockEntityTickInvoker> getBlockEntityTickers();
	//#endif
}
