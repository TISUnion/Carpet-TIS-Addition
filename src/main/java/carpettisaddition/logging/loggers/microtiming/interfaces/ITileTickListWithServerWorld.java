package carpettisaddition.logging.loggers.microtiming.interfaces;

import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public interface ITileTickListWithServerWorld
{
	/**
	 * Nullable if it's a custom ServerTickScheduler
	 * Since only value in vanilla ServerTickScheduler (block, fluid) has been assigned in {@link carpettisaddition.mixins.logger.microtiming.ServerWorldMixin}
	 */
	@Nullable
	ServerWorld getServerWorld();

	void setServerWorld(ServerWorld serverWorld);
}
