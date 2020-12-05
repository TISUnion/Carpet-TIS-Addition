package carpettisaddition.mixins.rule.optimizationFastEntityMovement;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.ReusableStream;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	private static final ThreadLocal<Boolean> optimizationFastEntityMovementEnable = ThreadLocal.withInitial(() -> false);
	private static final ThreadLocal<World> currentCollidingWorld = new ThreadLocal<>();
	private static final ThreadLocal<Entity> currentCollidingEntity = new ThreadLocal<>();

	// minimum velocity to trigger the optimization
	// set it to 0 to test vanilla behavior if you want
	private static final double OPTIMIZE_THRESHOLD = 4.0D;

	@Redirect(
			method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityContext;Lnet/minecraft/util/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;method_20812(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"
			)
	)
	private static Stream<VoxelShape> dontUseThatLargeBlockCollisions(World world, Entity entity, Box box, /* parent method parameters */ Entity entityParam, Vec3d movement, Box entityBoundingBox, World worldParam, EntityContext context, ReusableStream<VoxelShape> collisions)
	{
		optimizationFastEntityMovementEnable.set(CarpetTISAdditionSettings.optimizationFastEntityMovement && movement.lengthSquared() >= OPTIMIZE_THRESHOLD * OPTIMIZE_THRESHOLD);
		if (optimizationFastEntityMovementEnable.get())
		{
			currentCollidingEntity.set(entity);
			currentCollidingWorld.set(world);
			return Stream.empty();
		}
		return world.method_20812(entity, box);
	}

	@Redirect(
			method = "method_20737",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/util/stream/Stream;D)D"
			),
			require = 4
	)
	private static double useAxisOnlyBlockCollisions(Direction.Axis axis, Box box, Stream<VoxelShape> shapes, double maxDist, /* parent method parameters */ Vec3d movement, Box entityBoundingBox, ReusableStream<VoxelShape> collisions)
	{
		if (optimizationFastEntityMovementEnable.get())
		{
			Vec3d axisOnlyMovement = null;
			switch (axis)
			{
				case X:
					axisOnlyMovement = new Vec3d(movement.getX(), 0.0D, 0.0D);
					break;
				case Y:
					axisOnlyMovement = new Vec3d(0.0D, movement.getY(), 0.0D);
					break;
				case Z:
					axisOnlyMovement = new Vec3d(0.0D, 0.0D, movement.getZ());
					break;
			}
			Stream<VoxelShape> blockCollisions = currentCollidingWorld.get().method_20812(currentCollidingEntity.get(), entityBoundingBox.stretch(axisOnlyMovement));
			shapes = Stream.concat(blockCollisions, collisions.stream());
		}
		return VoxelShapes.calculateMaxOffset(axis, box, shapes, maxDist);
	}
}
