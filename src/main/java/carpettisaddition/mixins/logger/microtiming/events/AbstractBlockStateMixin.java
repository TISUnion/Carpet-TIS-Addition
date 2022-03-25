package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingMixinGlobals;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin
{
	@Inject(method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("HEAD"))
	private void startScheduleStateUpdate(WorldAccess worldAccess, BlockPos pos, int flags, int maxUpdateDepth, CallbackInfo ci)
	{
		if (worldAccess instanceof World)
		{
			MicroTimingMixinGlobals.prevStateUpdateEvent.set(null);
			MicroTimingLoggerManager.onScheduleBlockUpdate((World)worldAccess, pos, worldAccess.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null);
		}
	}
}
