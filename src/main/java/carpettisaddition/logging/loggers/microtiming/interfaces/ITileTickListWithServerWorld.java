package carpettisaddition.logging.loggers.microtiming.interfaces;

import carpettisaddition.mixins.logger.microtiming.hooks.ServerWorldMixin;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

/**
 * Used in mc 1.18+, where the game's tiletick list class doesn't store related ServerWorld object
 */
public interface ITileTickListWithServerWorld
{
	/**
	 * Nullable if it's a custom ServerTickScheduler
	 * Since only value in vanilla ServerTickScheduler (block, fluid) has been assigned in {@link ServerWorldMixin}
	 */
	@Nullable
	ServerWorld getServerWorld();

	void setServerWorld(ServerWorld serverWorld);
}