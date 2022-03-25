package carpettisaddition.mixins.logger.microtiming;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.logging.loggers.microtiming.interfaces.ServerWorldWithMicroTimingLogger;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldWithMicroTimingLogger
{
	@Shadow @Final private WorldTickScheduler<Block> blockTickScheduler;
	@Shadow @Final private WorldTickScheduler<Fluid> fluidTickScheduler;

	private MicroTimingLogger microTimingLogger;

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int i)
	{
		super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed, i);
	}

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTimingLogger = new MicroTimingLogger((ServerWorld)(Object)this);
		((ITileTickListWithServerWorld)this.blockTickScheduler).setServerWorld((ServerWorld)(Object)this);
		((ITileTickListWithServerWorld)this.fluidTickScheduler).setServerWorld((ServerWorld)(Object)this);
	}

	@Override
	public MicroTimingLogger getMicroTimingLogger()
	{
		return this.microTimingLogger;
	}
}
