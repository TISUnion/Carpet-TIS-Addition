package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(LookTargetUtil.class)
public class LookTargetUtilMixin {
    @Inject(
            method = "give",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private static void onThrowItemEntityLifeTimeTracker(LivingEntity entity, ItemStack stack, LivingEntity target, CallbackInfo ci, double ignored, ItemEntity itemEntity) {
        ((LifetimeTrackerTarget) itemEntity).recordSpawning(new MobThrowSpawningReason(entity.getType()));
    }
}
