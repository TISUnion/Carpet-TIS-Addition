package carpettisaddition.mixins.logger.lightqueue;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLightingProvider.class)
public interface ServerLightingProviderAccessor
{
	@Accessor
	ObjectList<Pair<ServerLightingProvider.Stage, Runnable>> getPendingTasks();
}
