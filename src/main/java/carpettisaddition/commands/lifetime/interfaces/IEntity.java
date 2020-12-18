package carpettisaddition.commands.lifetime.interfaces;

import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import net.minecraft.util.math.Vec3d;

public interface IEntity
{
	long getLifeTime();

	Vec3d getSpawningPosition();

	Vec3d getRemovalPosition();

	void recordSpawning(SpawningReason reason);

	void recordRemoval(RemovalReason reason);
}
