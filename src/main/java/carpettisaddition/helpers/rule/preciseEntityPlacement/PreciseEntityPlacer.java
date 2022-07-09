package carpettisaddition.helpers.rule.preciseEntityPlacement;

import carpettisaddition.utils.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Vec3d;

public class PreciseEntityPlacer
{
	public static final ThreadLocal<Vec3d> spawnEggTargetPos = ThreadLocal.withInitial(() -> null);

	public static void adjustEntity(Entity entity, Vec3d targetPos)
	{
		entity.refreshPositionAndAngles(targetPos.getX(), targetPos.getY(), targetPos.getZ(), EntityUtil.getYaw(entity), EntityUtil.getPitch(entity));
	}
	public static void adjustEntity(Entity entity, ItemUsageContext context)
	{
		adjustEntity(entity, context.getHitPos());
	}

	/**
	 * The spawnEggTargetPos should be set in SpawnEggItemMixin in advanced
	 */
	public static void adjustEntityFromSpawnEgg(Entity entity)
	{
		Vec3d vec3d = spawnEggTargetPos.get();
		if (vec3d != null)
		{
			adjustEntity(entity, vec3d);
			spawnEggTargetPos.remove();
		}
	}
}
