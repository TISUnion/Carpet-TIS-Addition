package carpettisaddition.mixins.logger.microtiming.events.tiletick;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Tile Tick
 * for MC 1.18+, executing tile tick stuffs are mixined in TileTickListMixin
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
}
