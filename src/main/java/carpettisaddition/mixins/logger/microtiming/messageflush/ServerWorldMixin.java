package carpettisaddition.mixins.logger.microtiming.messageflush;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World
{
	protected ServerWorldMixin(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient)
	{
		super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
	}

	@Inject(
			method = "tick",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickTime()V")
	)
	private void flushMessageOnTimeUpdate(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.microTimingTickDivision == TickDivision.WORLD_TIMER)
		{
			if (this.getDimension().getType() == DimensionType.OVERWORLD)  // only flush messages at overworld time update
			{
				MicroTimingLoggerManager.flushMessages();
			}
		}
	}
}
