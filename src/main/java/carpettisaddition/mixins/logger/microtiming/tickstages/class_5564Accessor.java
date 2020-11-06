package carpettisaddition.mixins.logger.microtiming.tickstages;

import net.minecraft.class_5562;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldChunk.class_5564.class)
public interface class_5564Accessor
{
	@Accessor("field_27228")
	class_5562 getTicking();
}
