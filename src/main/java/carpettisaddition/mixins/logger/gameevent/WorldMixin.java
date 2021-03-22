package carpettisaddition.mixins.logger.gameevent;

import carpettisaddition.logging.loggers.gameevent.GameEventLogger;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin
{
    @Inject(
            method = "emitGameEvent",
            at = @At("HEAD")
    )
    private void onGameEventStartProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range, CallbackInfo cir)
    {
        if (GameEventLogger.getInstance().isLoggerActivated())
        {
            GameEventLogger.getInstance().onGameEventStartProcessing(entity, gameEvent, pos, range);
        }
    }

    @Inject(
            method = "emitGameEvent",
            at = @At("TAIL")
    )
    private void onGameEventEndProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range, CallbackInfo cir)
    {
        if (GameEventLogger.getInstance().isLoggerActivated())
        {
            GameEventLogger.getInstance().onGameEventEndProcessing(entity, gameEvent, pos, range);
        }
    }

}
