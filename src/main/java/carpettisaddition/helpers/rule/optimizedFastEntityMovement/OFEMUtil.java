package carpettisaddition.helpers.rule.optimizedFastEntityMovement;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class OFEMUtil
{
	// minimum velocity to trigger the optimization
	// set it to 0 or enable rule ultraSecretSetting to test vanilla behavior if you want
	private static final double OPTIMIZE_THRESHOLD = 4.0D;

	private static boolean checkMovement(Vec3d movement)
	{
		return movement.lengthSquared() >= OPTIMIZE_THRESHOLD * OPTIMIZE_THRESHOLD || CarpetTISAdditionSettings.ultraSecretSetting.equals("optimizedFastEntityMovement");
	}

	@Nullable
	public static OFEMContext checkAndCreateContext(World world, Entity entity, Vec3d movement)
	{
		if (CarpetTISAdditionSettings.optimizedFastEntityMovement && checkMovement(movement))
		{
			return new OFEMContext(world, entity);
		}
		return null;
	}

	public static
	//#if MC >= 11800
	//$$ Iterable<VoxelShape>
	//#else
	Stream<VoxelShape>
	//#endif
	getAxisOnlyBlockCollision(OFEMContext context, CollisionBoxGetter collisionBoxGetter)
	{
		Vec3d axisOnlyMovement = null;
		switch (context.axis)
		{
			case X:
				axisOnlyMovement = new Vec3d(context.movementOnAxis, 0.0D, 0.0D);
				break;
			case Y:
				axisOnlyMovement = new Vec3d(0.0D, context.movementOnAxis, 0.0D);
				break;
			case Z:
				axisOnlyMovement = new Vec3d(0.0D, 0.0D, context.movementOnAxis);
				break;
		}
		return collisionBoxGetter.get(context.world, context.entity, context.entityBoundingBox.stretch(axisOnlyMovement));
	}

	public static
	//#if MC >= 11800
	//$$ Iterable<VoxelShape>
	//#else
	Stream<VoxelShape>
	//#endif
	getAxisOnlyBlockCollision(OFEMContext context)
	{
		// using vanilla's method
		return getAxisOnlyBlockCollision(context, CollisionView::getBlockCollisions);
	}
}