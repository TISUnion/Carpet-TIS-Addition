package carpettisaddition.mixins.carpet;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// for extension logging supports
@Mixin(LoggerRegistry.class)
public interface LoggerRegistryInvoker
{
	@Invoker
	static void callRegisterLogger(String name, Logger logger)
	{
	}
}
