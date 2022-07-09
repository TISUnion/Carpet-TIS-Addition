package carpettisaddition.mixins.logger.microtiming.hooks;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.interfaces.ServerWorldWithMicroTimingLogger;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11800
//$$ import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
//$$ import net.minecraft.block.Block;
//$$ import net.minecraft.fluid.Fluid;
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements ServerWorldWithMicroTimingLogger
{
	private MicroTimingLogger microTimingLogger;

	//#if MC >= 11800
	//$$ @Shadow @Final private WorldTickScheduler<Block> blockTickScheduler;
	//$$ @Shadow @Final private WorldTickScheduler<Fluid> fluidTickScheduler;
	//#endif

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct_microTimingLogger(CallbackInfo ci)
	{
		this.microTimingLogger = new MicroTimingLogger((ServerWorld)(Object)this);

		//#if MC >= 11800
		//$$ ((ITileTickListWithServerWorld)this.blockTickScheduler).setServerWorld((ServerWorld)(Object)this);
		//$$ ((ITileTickListWithServerWorld)this.fluidTickScheduler).setServerWorld((ServerWorld)(Object)this);
		//#endif
	}

	@Override
	public MicroTimingLogger getMicroTimingLogger()
	{
		return this.microTimingLogger;
	}
}
