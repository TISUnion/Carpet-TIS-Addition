package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
//$$ import net.minecraft.block.AbstractBlock;
//#endif

/**
 * Traditional block / state update stuffs, before mc 1.19
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(
		//#if MC >= 11600
		//$$ AbstractBlock.AbstractBlockState.class
		//#else
		Block.class
		//#endif
)
public abstract class BlockMixin
{
	@Inject(
			//#if MC >= 11600
			//$$ method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V",
			//#else
			method = "updateNeighborStates",
			//#endif
			at = @At("HEAD")
	)
	private void startStateUpdate(
			//#if MC >= 11600
			//$$ WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth,
			//#else
			BlockState state, IWorld world, BlockPos pos, int flags,
			//#endif
			CallbackInfo ci
	)
	{
		if (world instanceof World)
		{
			MicroTimingLoggerManager.onBlockUpdate((World)world, pos, world.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_START);
		}
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V",
			//#else
			method = "updateNeighborStates",
			//#endif
			at = @At("RETURN")
	)
	private void endStateUpdate(
			//#if MC >= 11600
			//$$ WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth,
			//#else
			BlockState state, IWorld world, BlockPos pos, int flags,
			//#endif
			CallbackInfo ci
	)
	{
		if (world instanceof World)
		{
			MicroTimingLoggerManager.onBlockUpdate((World)world, pos, world.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}
