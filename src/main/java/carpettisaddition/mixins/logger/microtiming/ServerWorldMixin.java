package carpettisaddition.mixins.logger.microtiming;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.interfaces.IServerWorld;
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
public abstract class ServerWorldMixin extends World implements IServerWorld
{
	private MicroTimingLogger microTimingLogger;

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
		this.microTimingLogger = new MicroTimingLogger((ServerWorld)(Object)this);
	}

	@Override
	public MicroTimingLogger getMicroTimingLogger()
	{
		return this.microTimingLogger;
	}
}
