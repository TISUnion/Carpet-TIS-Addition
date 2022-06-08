package carpettisaddition.mixins.rule.simpleNeighborUpdaterEnabled;

import carpettisaddition.helpers.rule.simpleNeighborUpdater.MutableNeighborUpdater;
import net.minecraft.world.World;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import net.minecraft.world.block.NeighborUpdater;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class WorldMixin {
    @Mutable
    @Final
    @Shadow
    protected NeighborUpdater neighborUpdater;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;neighborUpdater:Lnet/minecraft/world/block/NeighborUpdater;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void mixinWorld(World world, NeighborUpdater updater) {
        this.neighborUpdater = new MutableNeighborUpdater(world, (ChainRestrictedNeighborUpdater) updater);
    }
}
