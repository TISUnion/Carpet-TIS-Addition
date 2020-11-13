package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import me.jellysquid.mods.lithium.common.world.scheduler.TickEntry;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(LithiumServerTickScheduler.class)
public abstract class LithiumServerTickSchedulerMixin<T> extends ServerTickScheduler<T>
{
	@Shadow(remap = false) @Final private ServerWorld world;

	private boolean scheduleSuccess;

	public LithiumServerTickSchedulerMixin(ServerWorld world, Predicate<T> invalidObjPredicate, Function<T, Identifier> idToName, Consumer<ScheduledTick<T>> consumer)
	{
		super(world, invalidObjPredicate, idToName, consumer);
	}

	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	{
		this.scheduleSuccess = false;
	}

	@Inject(
			method = "addScheduledTick",
			at = @At(
					value = "FIELD",
					target = "Lme/jellysquid/mods/lithium/common/world/scheduler/TickEntry;scheduled:Z",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private void checkIfItIsNotScheduled(ScheduledTick<T> tick, CallbackInfo ci, TickEntry<T> entry)
	{
		this.scheduleSuccess = !entry.scheduled;
	}

	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		if (object instanceof Block)
		{
			MicroTimingLoggerManager.onScheduleTileTickEvent(this.world, (Block)object, pos, delay, priority, this.scheduleSuccess);
		}
	}
}
