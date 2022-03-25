package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldTickScheduler.class)
public abstract class WorldTickSchedulerMixin<T>
{
	private int oldListSize;

	@Inject(
			method = "scheduleTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void startScheduleTileTickEvent(OrderedTick<T> orderedTick, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	{
		this.oldListSize = chunkTickScheduler.getTickCount();
	}

	@Inject(
			method = "scheduleTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void endScheduleTileTickEvent(OrderedTick<T> tt, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			int delay = (int)(tt.triggerTick() - serverWorld.getTime());
			MicroTimingLoggerManager.onScheduleTileTickEvent(serverWorld, tt.type(), tt.pos(), delay, tt.priority(), chunkTickScheduler.getTickCount() > this.oldListSize);
		}
	}

	@ModifyVariable(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					remap = false
			)
	)
	private OrderedTick<T> preExecuteBlockTileTickEvent(OrderedTick<T> event)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_START);
		}
		return event;
	}

	@ModifyVariable(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER,
					remap = false
			)
	)
	private OrderedTick<T> postExecuteBlockTileTickEvent(OrderedTick<T> event)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_START);
		}
		return event;
	}
}
