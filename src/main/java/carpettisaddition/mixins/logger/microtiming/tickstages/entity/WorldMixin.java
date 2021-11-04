package carpettisaddition.mixins.logger.microtiming.tickstages.entity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.WorldWithEntityTickingOrder;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.EntitySubStage;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin implements WorldWithEntityTickingOrder
{
	private int entityOrderCounter;

	public void setEntityOrderCounter(int value)
	{
		this.entityOrderCounter = value;
	}

	@Inject(method = "tickEntity", at = @At("HEAD"))
	private void beforeTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage((World)(Object)this, new EntitySubStage(entity, this.entityOrderCounter++));
	}

	@Inject(method = "tickEntity", at = @At("RETURN"))
	private void afterTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage((World)(Object)this, null);
	}
}
