package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.spawning.SpawningReason;

/**
 * Used in mc 1.17+
 */
public class LifetimeMixinUtil
{
	/**
	 * a global field to pass the spawning reason for the xp orb
	 * so lifetime tracker is able to track xp orb's spawning reason in static method ExperienceOrbEntity#spawn
 	 */
	public static final ThreadLocal<SpawningReason> xpOrbSpawningReason = ThreadLocal.withInitial(() -> null);
}