package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.utils.IdentifierUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTrackerUtil
{
	public static boolean isTrackedEntityClass(Entity entity)
	{
		return entity instanceof MobEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity;
	}

	public static String getEntityTypeDescriptor(EntityType<?> entityType)
	{
		return IdentifierUtil.id(entityType).getPath();
	}

	public static Optional<EntityType<?>> getEntityTypeFromName(String name)
	{
		return Registry.ENTITY_TYPE.stream().filter(entityType -> getEntityTypeDescriptor(entityType).equals(name)).findFirst();
	}

	public static Stream<String> getEntityTypeDescriptorStream()
	{
		return Registry.ENTITY_TYPE.stream().map(LifeTimeTrackerUtil::getEntityTypeDescriptor);
	}
}
