package carpettisaddition.mixins.logger.microtiming.tickstages.chunktick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(method = "tickChunk", at = @At("HEAD"))
	private void enterTickChunk(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.CHUNK_TICK);
	}

	@Inject(method = "tickChunk", at = @At("TAIL"))
	private void exitTickChunk(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=thunder"
			)
	)
	private void onStageDetailThunder(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Thunder");
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=iceandsnow"
			)
	)
	private void onStageDetailIceAndSnow(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Ice&Snow");
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickBlocks"
			)
	)
	private void onStageDetailRandomTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "RandomTick");
	}
}
