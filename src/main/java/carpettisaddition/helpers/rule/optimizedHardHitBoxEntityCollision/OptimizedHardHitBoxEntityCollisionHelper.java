package carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public class OptimizedHardHitBoxEntityCollisionHelper
{
	public static final ThreadLocal<Boolean> checkHardHitBoxEntityOnly = ThreadLocal.withInitial(() -> false);

	/**
	 * Check if the given entity may have {@link Entity#collidesWith} return true if other entity has {@link Entity#isCollidable} returns false
	 * In vanilla only boat and minecart do that
	 *
	 * Doesn't work with other mods that add new type of entities
	 */
	public static boolean treatsGeneralEntityAsHardHitBox(Entity entity)
	{
		return entity instanceof BoatEntity || entity instanceof AbstractMinecartEntity;
	}

	/**
	 * Check if the given entity may have hard hitbox
	 * In vanilla only boat and shulker have hard hitbox
	 * For boat, Entity#getCollisionBox always returns not null
	 * For shulker, Entity#getCollisionBox returns not null if the shulker is alive, in case of edge cases
	 * here comes an extra shulker check
	 *
	 * Might not work with other mods since they might have dynamic Entity#getCollisionBox result
	 *
	 * ----- ^ doc for <= 1.15.2 -----
	 * In 1.16 there's a method called {@link Entity#isCollidable}, which handles this logic well, so just use it
	 */
	public static boolean hasHardHitBox(Entity entity)
	{
		return entity.isCollidable();
	}
}
