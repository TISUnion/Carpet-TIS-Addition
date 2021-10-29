package carpettisaddition.mixins.logger.microtiming.events;

import net.minecraft.class_6755;
import net.minecraft.class_6760;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(class_6755.class)
public interface ChunkTileTickListAccessor
{
	@Accessor("field_35527")
	Queue<class_6760<?>> getQueue();
}
