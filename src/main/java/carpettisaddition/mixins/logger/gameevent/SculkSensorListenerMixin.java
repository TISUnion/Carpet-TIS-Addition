package carpettisaddition.mixins.logger.gameevent;

import carpettisaddition.logging.loggers.gameevent.GameEventLogger;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.SculkSensorListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkSensorListener.class)
public class SculkSensorListenerMixin
{
    @Inject(
            method = "isOccluded",
            at = @At("HEAD")
    )
    private void isOccluded(World world, BlockPos pos, BlockPos sourcePos, CallbackInfoReturnable<Boolean> cir)
    {
        if (!GameEventLogger.getInstance().isLoggerActivated())
        {
            return;
        }
        HitResult hitResult = world.raycast(new BlockStateRaycastContext(Vec3d.ofCenter(pos), Vec3d.ofCenter(sourcePos), (state) ->
                state.isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)));
        if (hitResult.getType() == HitResult.Type.BLOCK)
        {
            // This GameEvent will be occluded by a special block
            // wools etc
            GameEventLogger.getInstance().onGameEventOccluded(hitResult);
        }
    }

    @Inject(
            method = "listen(Lnet/minecraft/world/World;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At("HEAD")
    )
    private void onGameEventSensed(World world, GameEvent gameEvent, BlockPos blockPos, BlockPos sourcePos, CallbackInfo ci)
    {
        if (!GameEventLogger.getInstance().isLoggerActivated())
        {
            return;
        }
        GameEventLogger.getInstance().onGameEventSculkSensed(world, gameEvent, blockPos, sourcePos);
    }
}
