package carpettisaddition.mixins.option.keepMobInLazyChunks;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
    /*
    No need in 1.14

    @Redirect(method = "tick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/Entity;checkDespawn()V"
    ))
    private void dummyCheckDespawn(Entity entity) {
        if(!CarpetTISAdditionSettings.keepMobInLazyChunks)
            entity.checkDespawn();
    }
     */
}
