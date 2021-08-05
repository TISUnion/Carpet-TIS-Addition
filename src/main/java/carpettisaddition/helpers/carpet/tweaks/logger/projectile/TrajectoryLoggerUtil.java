package carpettisaddition.helpers.carpet.tweaks.logger.projectile;

import net.minecraft.entity.Entity;

public class TrajectoryLoggerUtil
{
	public static final ThreadLocal<Entity> currentEntity = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isVisualizer = ThreadLocal.withInitial(() -> false);
}
