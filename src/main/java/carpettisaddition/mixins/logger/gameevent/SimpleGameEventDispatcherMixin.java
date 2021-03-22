package carpettisaddition.mixins.logger.gameevent;

import carpettisaddition.logging.loggers.gameevent.GameEventLogger;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.SimpleGameEventDispatcher;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleGameEventDispatcher.class)
public class SimpleGameEventDispatcherMixin
{
    @Inject(
            method = "dispatchTo",
            at = @At("HEAD")
    )
    private void onGameEventCaught(World world, GameEvent event, @Nullable Entity entity, BlockPos pos, GameEventListener listener, CallbackInfoReturnable<Boolean> cir)
    {
        if (GameEventLogger.getInstance().isLoggerActivated())
        {
            GameEventLogger.getInstance().onGameEventListen(world, listener);
        }
    }
}
