package carpettisaddition.mixins.option;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ServerWorld.class)
public abstract class ServerWorld_keepMobInLazyChunksMixin {

    @Redirect(method = "tick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/Entity;checkDespawn()V"
    ))
    private void dummyCheckDespawn(Entity entity) {
        if(!CarpetTISAdditionSettings.keepMobInLazyChunks)
            entity.checkDespawn();
    }
}
