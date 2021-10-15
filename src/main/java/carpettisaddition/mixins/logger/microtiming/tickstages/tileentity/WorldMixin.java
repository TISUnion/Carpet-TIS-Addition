package carpettisaddition.mixins.logger.microtiming.tickstages.tileentity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileEntitySubStage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(World.class)
public abstract class WorldMixin
{
	private int tileEntityOrderCounter;

	@Inject(method = "tickBlockEntities", at = @At("HEAD"))
	private void enterStageTileEntity(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((World)(Object)this, TickStage.TILE_ENTITY);
		this.tileEntityOrderCounter = 0;
	}

	@Inject(method = "tickBlockEntities", at = @At("TAIL"))
	private void exitStageTileEntity(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((World)(Object)this, TickStage.UNKNOWN);
	}

	@Inject(
			method = "tickBlockEntities",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/Tickable;tick()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onTickTileEntity(CallbackInfo ci, Profiler profiler, Iterator<?> iterator, BlockEntity blockEntity)
	{
		MicroTimingLoggerManager.setSubTickStage((World)(Object)this, new TileEntitySubStage(blockEntity, this.tileEntityOrderCounter++));  // TISCM Micro Tick logger
	}
}
