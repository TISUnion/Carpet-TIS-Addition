package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import net.minecraft.class_6757;
import net.minecraft.class_6760;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiConsumer;

// ServerTickScheduler in 1.17
@Mixin(class_6757.class)
public abstract class ServerTickSchedulerMixin<T>
{
	private int tileTickOrderCounter;

	@Inject(method = "method_39390", at = @At("HEAD"))
	private void startExecutingTileTickEvents(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(
			method = "method_39390",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void beforeExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci, class_6760<?> lv)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setSubTickStage(serverWorld, new TileTickSubStage(serverWorld, lv, this.tileTickOrderCounter++));
		}
	}

	@Inject(
			method = "method_39390",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void afterExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci, class_6760<?> lv)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setSubTickStage(serverWorld, null);
		}
	}
}
