package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingMixinGlobals;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChainRestrictedNeighborUpdater.class)
public abstract class ChainRestrictedNeighborUpdaterMixin
{
	@ModifyArg(
			method = "method_42392",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/block/ChainRestrictedNeighborUpdater;enqueue(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/block/ChainRestrictedNeighborUpdater$Entry;)V",
					remap = false
			),
			index = 1,
			remap = false
	)
	private ChainRestrictedNeighborUpdater.Entry microTimingMarkStateUpdateEventStartEnd(ChainRestrictedNeighborUpdater.Entry entry)
	{
		if (entry instanceof MicroTimingMixinGlobals.ContinuouslyEvent && MicroTimingLoggerManager.isLoggerActivated())
		{
			MicroTimingMixinGlobals.ContinuouslyEvent currentEvent = (MicroTimingMixinGlobals.ContinuouslyEvent)entry;
			MicroTimingMixinGlobals.ContinuouslyEvent prevEvent = MicroTimingMixinGlobals.prevStateUpdateEvent.get();
			if (prevEvent != null)
			{
				prevEvent.setIsEnd(false);
			}
			else
			{
				currentEvent.setIsStart(true);
			}
			currentEvent.setIsEnd(true);
			MicroTimingMixinGlobals.prevStateUpdateEvent.set(currentEvent);
		}
		return entry;
	}
}
