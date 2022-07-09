package carpettisaddition.mixins.logger.microtiming.events.compat.lithium;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import me.jellysquid.mods.lithium.common.world.scheduler.TickEntry;
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

@Restriction(require = @Condition(ModIds.lithium))
@Mixin(LithiumServerTickScheduler.class)
public abstract class LithiumServerTickSchedulerMixin<T> extends ServerTickScheduler<T>
{
	@Shadow(remap = false) @Final private ServerWorld world;

	private boolean scheduleSuccess;

	public LithiumServerTickSchedulerMixin(
			ServerWorld world, Predicate<T> invalidObjPredicate, Function<T, Identifier> idToName,
			//#if MC < 11600
			Function<Identifier, T> nameToId,
			//#endif
			Consumer<ScheduledTick<T>> scheduledTickConsumer
	)
	{
		super(
				world, invalidObjPredicate, idToName,
				//#if MC < 11600
				nameToId,
				//#endif
				scheduledTickConsumer
		);
	}

	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	{
		this.scheduleSuccess = false;
	}

	@Inject(
			//#if MC >= 11700
			//$$ method = "scheduleTick",
			//$$ at = @At(
			//$$ 		value = "INVOKE_ASSIGN",
			//$$ 		target = "Lit/unimi/dsi/fastutil/objects/ObjectOpenHashSet;add(Ljava/lang/Object;)Z",
			//$$ 		ordinal = 0,
			//$$ 		remap = false
			//$$ ),
			//#else
			method = "addScheduledTick",
			at = @At(
					value = "FIELD",
					target = "Lme/jellysquid/mods/lithium/common/world/scheduler/TickEntry;scheduled:Z",
					ordinal = 0
			),
			//#endif
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private void checkScheduledState(
			//#if MC >= 11700
			//$$ BlockPos pos, Object object, long time, TickPriority priority, CallbackInfo ci, TickEntry<T> tick, boolean added
			//#else
			ScheduledTick<T> tick, CallbackInfo ci, TickEntry<T> entry
			//#endif
	)
	{
		this.scheduleSuccess =
				//#if MC >= 11700
				//$$ added;
				//#else
				!entry.scheduled;
				//#endif
	}

	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onScheduleTileTickEvent(this.world, object, pos, delay, priority, this.scheduleSuccess);
	}
}
