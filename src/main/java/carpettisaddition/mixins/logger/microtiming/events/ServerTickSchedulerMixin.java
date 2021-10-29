package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import net.minecraft.class_6755;
import net.minecraft.class_6757;
import net.minecraft.class_6760;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


// ServerTickScheduler in 1.17
@Mixin(class_6757.class)
public abstract class ServerTickSchedulerMixin<T>
{
	private int oldListSize;

	@Inject(
			method = "method_39363",  // scheduleTileTick stuff
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_6755;method_39363(Lnet/minecraft/class_6760;)V"  // schedule tileTick in chunk
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void startScheduleTileTickEvent(class_6760<T> tt, CallbackInfo ci, long l, class_6755<T> chunkTickScheduler)
	{
		this.oldListSize = chunkTickScheduler.getTicks();
	}

	@Inject(
			method = "method_39363",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_6755;method_39363(Lnet/minecraft/class_6760;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void endScheduleTileTickEvent(class_6760<T> tt, CallbackInfo ci, long l, class_6755<T> chunkTickScheduler)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			int delay = (int)(tt.triggerTick() - serverWorld.getTime());
			MicroTimingLoggerManager.onScheduleTileTickEvent(serverWorld, tt.type(), tt.pos(), delay, tt.priority(), chunkTickScheduler.getTicks() > this.oldListSize);
		}
	}
}
