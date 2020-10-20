package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
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
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Pseudo
@Mixin(LithiumServerTickScheduler.class)
public class LithiumServerTickSchedulerMixin<T> extends ServerTickScheduler<T>
{
	@Shadow(remap = false) @Final private Map<ScheduledTick<T>, TickEntry<T>> scheduledTicks;
	@Shadow(remap = false) @Final private ServerWorld world;

	private final ThreadLocal<Integer> oldListSize = ThreadLocal.withInitial(() -> 0);

	public LithiumServerTickSchedulerMixin(ServerWorld world, Predicate<T> invalidObjPredicate, Function<T, Identifier> idToName, Consumer<ScheduledTick<T>> consumer)
	{
		super(world, invalidObjPredicate, idToName, consumer);
	}

	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	{
		this.oldListSize.set(this.scheduledTicks.size());
	}

	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		if (object instanceof Block)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(this.world, (Block)object, pos, delay, priority, this.scheduledTicks.size() > oldListSize.get());
		}
	}
}
