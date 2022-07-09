package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

//#if MC >= 11800
//$$ import carpettisaddition.logging.loggers.microtiming.enums.EventType;
//$$ import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
//$$ import net.minecraft.world.tick.ChunkTickScheduler;
//$$ import net.minecraft.world.tick.OrderedTick;
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//#else
import net.minecraft.server.world.ServerTickScheduler;
//#endif

@Mixin(
		//#if MC >= 11800
		//$$ WorldTickScheduler.class
		//#else
		ServerTickScheduler.class
		//#endif
)
public abstract class TileTickListMixin<T>
{
	//#if MC < 11800
	@Shadow @Final private ServerWorld world;
	@Shadow @Final private Set<ScheduledTick<T>> scheduledTickActions;
	//#endif

	private int oldListSize;

	//#if MC >= 11800
	//$$ @Inject(
	//$$ 		method = "scheduleTick",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V"
	//$$ 		),
	//$$ 		locals = LocalCapture.CAPTURE_FAILHARD
	//$$ )
	//$$ private void startScheduleTileTickEvent(OrderedTick<T> orderedTick, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	//#else
	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	//#endif
	{
		this.oldListSize =
				//#if MC >= 11800
				//$$ chunkTickScheduler.getTickCount();
				//#else
				this.scheduledTickActions.size();
				//#endif
	}

	//#if MC >= 11800
	//$$ @Inject(
	//$$ 		method = "scheduleTick",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V",
	//$$ 				shift = At.Shift.AFTER
	//$$ 		),
	//$$ 		locals = LocalCapture.CAPTURE_FAILHARD
	//$$ )
	//$$ private void endScheduleTileTickEvent(OrderedTick<T> tt, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	//$$ {
	//$$ 	ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
	//$$ 	if (serverWorld != null)
	//$$ 	{
	//$$ 		int delay = (int)(tt.triggerTick() - serverWorld.getTime());
	//$$ 		MicroTimingLoggerManager.onScheduleTileTickEvent(serverWorld, tt.type(), tt.pos(), delay, tt.priority(), chunkTickScheduler.getTickCount() > this.oldListSize);
	//$$ 	}
	//$$ }
	//#else
	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onScheduleTileTickEvent(this.world, object, pos, delay, priority, this.scheduledTickActions.size() > this.oldListSize);
	}
	//#endif


	// execute tile tick events, for mc1.18+

	//#if MC >= 11800
	//$$ @ModifyVariable(
	//$$ 		method = "tick(Ljava/util/function/BiConsumer;)V",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
	//$$ 				remap = false
	//$$ 		)
	//$$ )
	//$$ private OrderedTick<T> preExecuteBlockTileTickEvent(OrderedTick<T> event)
	//$$ {
	//$$ 	ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
	//$$ 	if (serverWorld != null)
	//$$ 	{
	//$$ 		MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_START);
	//$$ 	}
	//$$ 	return event;
	//$$ }
	//$$
	//$$ @ModifyVariable(
	//$$ 		method = "tick(Ljava/util/function/BiConsumer;)V",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
	//$$ 				shift = At.Shift.AFTER,
	//$$ 				remap = false
	//$$ 		)
	//$$ )
	//$$ private OrderedTick<T> postExecuteBlockTileTickEvent(OrderedTick<T> event)
	//$$ {
	//$$ 	ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
	//$$ 	if (serverWorld != null)
	//$$ 	{
	//$$ 		MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_END);
	//$$ 	}
	//$$ 	return event;
	//$$ }
	//#endif
}
