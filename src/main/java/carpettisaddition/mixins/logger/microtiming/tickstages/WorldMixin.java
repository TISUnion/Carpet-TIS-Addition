package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.interfaces.IWorld_microTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld_microTimingLogger
{
	/*
	 * ---------------
	 *  Tile Entities
	 * ---------------
	 */

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

	/*
	 * ----------
	 *  Entities
	 * ----------
	 */

	private int entityOrderCounter;

	public void setEntityOrderCounter(int value)
	{
		this.entityOrderCounter = value;
	}

	@Inject(method = "tickEntity", at = @At("HEAD"))
	private void beforeTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageExtra((World)(Object)this, new EntityTickStageExtra(entity, this.entityOrderCounter++));
	}

	@Inject(method = "tickEntity", at = @At("RETURN"))
	private void afterTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageExtra((World)(Object)this, null);
	}
}
