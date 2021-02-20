package carpettisaddition.mixins.rule.synchronizedLightThread;

import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.util.thread.TaskExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLightingProvider.class)
public interface ServerLightingProviderAccessor
{
	@Accessor
	TaskExecutor<Runnable> getProcessor();
}
