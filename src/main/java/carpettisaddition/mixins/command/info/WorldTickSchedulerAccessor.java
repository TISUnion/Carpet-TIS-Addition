package carpettisaddition.mixins.command.info;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldTickScheduler.class)
public interface WorldTickSchedulerAccessor<T>
{
	@Accessor
	Long2ObjectMap<ChunkTickScheduler<T>> getChunkTickSchedulers();
}
