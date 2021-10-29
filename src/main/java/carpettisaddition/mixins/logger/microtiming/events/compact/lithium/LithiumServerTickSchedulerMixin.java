package carpettisaddition.mixins.logger.microtiming.events.compact.lithium;

import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LithiumServerTickScheduler.class)
public abstract class LithiumServerTickSchedulerMixin<T>
{
//	@Shadow(remap = false) @Final private ServerWorld world;
//
//	private boolean scheduleSuccess;
//
//	public LithiumServerTickSchedulerMixin(ServerWorld world, Predicate<T> invalidObjPredicate, Function<T, Identifier> idToName, Consumer<ScheduledTick<T>> consumer)
//	{
//		super(world, invalidObjPredicate, idToName, consumer);
//	}
//
//	@Inject(method = "schedule", at = @At("HEAD"))
//	private void startScheduleTileTickEvent(CallbackInfo ci)
//	{
//		this.scheduleSuccess = false;
//	}
//
//	@Inject(
//			method = "scheduleTick",
//			at = @At(
//					value = "INVOKE_ASSIGN",
//					target = "Lit/unimi/dsi/fastutil/objects/ObjectOpenHashSet;add(Ljava/lang/Object;)Z",
//					ordinal = 0,
//					remap = false
//			),
//			locals = LocalCapture.CAPTURE_FAILHARD,
//			remap = false
//	)
//	private void checkIfItIsScheduled(BlockPos pos, Object object, long time, TickPriority priority, CallbackInfo ci, TickEntry<T> tick, boolean added)
//	{
//		this.scheduleSuccess = added;
//	}
//
//	@Inject(method = "schedule", at = @At("RETURN"))
//	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
//	{
//		MicroTimingLoggerManager.onScheduleTileTickEvent(this.world, object, pos, delay, priority, this.scheduleSuccess);
//	}
}
