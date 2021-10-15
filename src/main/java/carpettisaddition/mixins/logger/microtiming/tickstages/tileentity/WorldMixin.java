package carpettisaddition.mixins.logger.microtiming.tickstages.tileentity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.interfaces.IWorldTileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements IWorldTileEntity
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

	@Override
	public int getTileEntityOrderCounter()
	{
		return this.tileEntityOrderCounter;
	}

	@Override
	public void setTileEntityOrderCounter(int tileEntityOrderCounter)
	{
		this.tileEntityOrderCounter = tileEntityOrderCounter;
	}
}
