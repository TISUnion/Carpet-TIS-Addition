package carpettisaddition.mixins.command.manipulate;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	@Accessor
	Int2ObjectMap<Entity> getEntitiesById();

	@Accessor("inEntityTick")
	boolean isTickingEntity();

	@Accessor
	ObjectLinkedOpenHashSet<BlockEvent> getSyncedBlockEventQueue();
}
