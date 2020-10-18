package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.interfaces.IServerWorld_MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLogger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements IServerWorld_MicroTickLogger
{
	private MicroTickLogger microTickLogger;

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed)
	{
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTickLogger = new MicroTickLogger((ServerWorld)(Object)this);
	}

	@Override
	public MicroTickLogger getMicroTickLogger()
	{
		return this.microTickLogger;
	}

	@Inject(
			method = "tickTime",
			at = @At(value = "HEAD")
	)
	private void onTimeUpdate(CallbackInfo ci)
	{
		if (this.getRegistryKey() == World.OVERWORLD)  // only flush messages at overworld time update
		{
			MicroTickLoggerManager.flushMessages(this.getTime());
		}
	}
}
