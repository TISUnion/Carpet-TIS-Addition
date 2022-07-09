package carpettisaddition.mixins.command.manipulate;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11700
//$$ import net.minecraft.world.EntityList;
//#else
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
//#endif

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	//#if MC >= 11700
	//$$ @Accessor
	//$$ EntityList getEntityList();
	//#endif

	//#if MC < 11700
	@Accessor
	Int2ObjectMap<Entity> getEntitiesById();

	@Accessor(
			//#if MC >= 11600
			//$$ "inEntityTick"
			//#else
			"ticking"
			//#endif
	)
	boolean isTickingEntity();
	//#endif

	@Accessor(
			//#if MC >= 11600
			//$$ "syncedBlockEventQueue"
			//#endif
	)
	ObjectLinkedOpenHashSet<BlockAction> getPendingBlockActions();
}
