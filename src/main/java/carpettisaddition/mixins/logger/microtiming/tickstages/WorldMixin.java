package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.interfaces.IWorld_microTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld_microTimingLogger
{
	private int tileEntityOrderCounter;

	@Override
	public int getTileEntityOrderCounter()
	{
		return tileEntityOrderCounter;
	}

	@Override
	public void setTileEntityOrderCounter(int tileEntityOrderCounter)
	{
		this.tileEntityOrderCounter = tileEntityOrderCounter;
	}

	@Inject(method = "tickBlockEntities", at = @At("HEAD"))
	private void onStageTileEntity(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((World)(Object)this, TickStage.TILE_ENTITY);
		this.tileEntityOrderCounter = 0;
	}
}
