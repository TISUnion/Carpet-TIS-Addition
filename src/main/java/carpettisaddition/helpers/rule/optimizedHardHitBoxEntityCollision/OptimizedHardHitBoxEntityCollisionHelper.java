package carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public class OptimizedHardHitBoxEntityCollisionHelper
{
	public static final ThreadLocal<Boolean> checkHardHitBoxEntityOnly = ThreadLocal.withInitial(() -> false);

	/**
	 * Check if the given entity may have entity.getHardCollisionBox return not null
	 * In vanilla only boat and minecart do that
	 *
	 * Doesn't work with other mods
	 */
	public static boolean treatsGeneralEntityAsHardHitBox(Entity entity)
	{
		return entity instanceof BoatEntity || entity instanceof AbstractMinecartEntity;
	}

	/**
	 * Check if the given entity may have hard hitbox
	 * In vanilla only boat and shulker have hard hitbox
	 * For boat, getCollisionBox always returns not null
	 * For shulker, getCollisionBox returns not null if the shulker is alive, in case of edge cases
	 * here comes an extra shulker check
	 *
	 * Might not work with other mods
	 */
	public static boolean hasHardHitBox(Entity entity)
	{
		return entity.getCollisionBox() != null || entity instanceof ShulkerEntity;
	}
}
