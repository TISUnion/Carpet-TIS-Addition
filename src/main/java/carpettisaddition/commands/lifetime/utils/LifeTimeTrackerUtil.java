package carpettisaddition.commands.lifetime.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;

public class LifeTimeTrackerUtil
{
	public static boolean isTrackedEntity(Entity entity)
	{
		return entity instanceof MobEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity;
	}

	public static String getEntityTypeDescriptor(EntityType<?> entityType)
	{
		return Registry.ENTITY_TYPE.getId(entityType).getPath();
	}
}
