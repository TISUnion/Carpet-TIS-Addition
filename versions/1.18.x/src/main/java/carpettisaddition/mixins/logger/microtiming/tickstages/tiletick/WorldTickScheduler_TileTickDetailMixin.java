package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiConsumer;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(WorldTickScheduler.class)
public abstract class WorldTickScheduler_TileTickDetailMixin
{
	private int tileTickOrderCounter;

	@Inject(method = "tick(Ljava/util/function/BiConsumer;)V", at = @At("HEAD"))
	private void startExecutingTileTickEvents(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void beforeExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci, OrderedTick<?> orderedTick)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setTickStageDetail(serverWorld, String.valueOf(orderedTick.priority().getIndex()));
			MicroTimingLoggerManager.setSubTickStage(serverWorld, new TileTickSubStage(serverWorld, orderedTick, this.tileTickOrderCounter++));
		}
	}

	@Inject(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER
			)
	)
	private void afterExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setTickStageDetail(serverWorld, null);
			MicroTimingLoggerManager.setSubTickStage(serverWorld, null);
		}
	}
}