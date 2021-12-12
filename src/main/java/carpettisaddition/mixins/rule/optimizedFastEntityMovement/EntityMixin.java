package carpettisaddition.mixins.rule.optimizedFastEntityMovement;

import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMContext;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Unique
	private static final ThreadLocal<OFEMContext> context = ThreadLocal.withInitial(() -> null);

	@Redirect(
			method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/lang/Iterable;"
			)
	)
	private static Iterable<VoxelShape> dontUseThatLargeBlockCollisions(World world, Entity entity, Box box, /* parent method parameters -> */ Entity entityParam, Vec3d movement, Box entityBoundingBox, World worldParam, List<VoxelShape> collisions)
	{
		OFEMContext ctx = OFEMUtil.checkAndCreateContext(world, entity, movement);
		context.set(ctx);
		if (ctx != null)
		{
			return Collections.emptyList();
		}
		// vanilla
		return world.getBlockCollisions(entity, box);
	}

	@Redirect(
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					ordinal = 0
			)
	)
	private static boolean theCollisionsListParameterIsIncompleteSoDontReturnEvenIfItIsEmpty(List<VoxelShape> voxelShapeList)
	{
		if (context.get() != null)
		{
			return false;
		}
		return voxelShapeList.isEmpty();
	}

	@Redirect(
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D"
			),
			require = 4
	)
	private static double useAxisOnlyBlockCollisions(Direction.Axis axis, Box box, Iterable<VoxelShape> shapes, double maxDist, /* parent method parameters -> */ Vec3d movement, Box entityBoundingBox, List<VoxelShape> collisionsExceptBlockCollisions)
	{
		OFEMContext ctx = context.get();
		if (ctx != null)
		{
			ctx.axis = axis;
			ctx.movementOnAxis = maxDist;
			ctx.entityBoundingBox = entityBoundingBox;
			Iterable<VoxelShape> blockCollisions = OFEMUtil.getAxisOnlyBlockCollision(ctx);
			List<VoxelShape> voxelShapeList = Lists.newArrayList();

			// order: (entity, border), block
			voxelShapeList.addAll(collisionsExceptBlockCollisions);
			blockCollisions.forEach(voxelShapeList::add);

			shapes = voxelShapeList;
		}
		return VoxelShapes.calculateMaxOffset(axis, box, shapes, maxDist);
	}
}
