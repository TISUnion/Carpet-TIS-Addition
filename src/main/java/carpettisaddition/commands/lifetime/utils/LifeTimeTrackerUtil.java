package carpettisaddition.commands.lifetime.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class LifeTimeTrackerUtil
{
	public static boolean isTrackedEntityType(EntityType<?> entityType)
	{
		return entityType.getSpawnGroup() != SpawnGroup.MISC || entityType == EntityType.ITEM || entityType == EntityType.EXPERIENCE_ORB;
	}

	public static boolean isTrackedEntity(Entity entity)
	{
		return isTrackedEntityType(entity.getType());
	}

	public static String getEntityTypeDescriptor(EntityType<?> entityType)
	{
		return Registry.ENTITY_TYPE.getId(entityType).getPath();
	}
}
