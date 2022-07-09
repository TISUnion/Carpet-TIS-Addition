package carpettisaddition.mixins.logger.microtiming.tickstages;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#endif

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	//#if MC >= 11800
	//$$ @Invoker
	//$$ boolean invokeIsTickingFutureReady(long l);
	//#endif
}