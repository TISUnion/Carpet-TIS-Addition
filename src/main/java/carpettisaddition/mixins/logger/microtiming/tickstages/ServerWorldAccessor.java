package carpettisaddition.mixins.logger.microtiming.tickstages;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	@Invoker
	boolean invokeIsTickingFutureReady(long l);
}
