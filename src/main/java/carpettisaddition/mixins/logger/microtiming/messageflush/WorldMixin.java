package carpettisaddition.mixins.logger.microtiming.messageflush;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow public abstract Dimension getDimension();

	@Shadow public abstract long getTime();

	@Inject(
			method = "tickTime",
			at = @At(value = "HEAD")
	)
	private void onTimeUpdate(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.microTimingTickDivision == TickDivision.WORLD_TIMER)
		{
			if (this.getDimension().getType() == DimensionType.OVERWORLD)  // only flush messages at overworld time update
			{
				MicroTimingLoggerManager.flushMessages(this.getTime());
			}
		}
	}
}
