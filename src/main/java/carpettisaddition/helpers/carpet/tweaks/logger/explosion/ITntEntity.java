package carpettisaddition.helpers.carpet.tweaks.logger.explosion;

import net.minecraft.util.math.Vec3d;

public interface ITntEntity
{
	boolean dataRecorded();

	Vec3d getInitializedVelocity();

	Vec3d getInitializedPosition();
}
