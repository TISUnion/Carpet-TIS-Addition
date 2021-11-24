package carpettisaddition.mixins.command.manipulate;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	@Accessor
	EntityList getEntityList();

	@Accessor
	ObjectLinkedOpenHashSet<BlockEvent> getSyncedBlockEventQueue();
}
