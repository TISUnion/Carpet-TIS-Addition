/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.optimizedFastEntityMovement;

import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMContext;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMUtil;
import carpettisaddition.utils.mixin.testers.LithiumEntityMovementOptimizationTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
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

//#if MC >= 11800
//$$ import com.google.common.collect.Lists;
//$$ import java.util.Collections;
//$$ import java.util.List;
//#else
import net.minecraft.util.ReusableStream;
import java.util.stream.Stream;
//#endif

@Restriction(conflict = @Condition(type = Condition.Type.TESTER, tester = LithiumEntityMovementOptimizationTester.class))
@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Unique
	private static final ThreadLocal<OFEMContext> ofemContext = ThreadLocal.withInitial(() -> null);

	@Redirect(
			//#if MC >= 11800
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11600
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Lnet/minecraft/block/ShapeContext;Lnet/minecraft/util/collection/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#else
			method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityContext;Lnet/minecraft/util/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/lang/Iterable;"
					//#elseif MC >= 11500
					target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"
					//#else
					//$$ target = "Lnet/minecraft/world/World;method_20812(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"
					//#endif
			)
	)
	private static
	//#if MC >= 11800
	//$$ Iterable<VoxelShape>
	//#else
	Stream<VoxelShape>
	//#endif
	dontUseThatLargeBlockCollisions(
			World world, Entity entity, Box box,
			/* parent method parameters vvv */

			Entity entityParam, Vec3d movement, Box entityBoundingBox, World worldParam,

			//#if MC >= 11800
			//$$ List<VoxelShape> collisions
			//#else
			EntityContext context, ReusableStream<VoxelShape> collisions
			//#endif
	)
	{
		OFEMContext ctx = OFEMUtil.checkAndCreateContext(world, entity, movement);
		ofemContext.set(ctx);
		if (ctx != null)
		{
			//#if MC >= 11800
			//$$ Collections.emptyList();
			//#else
			return Stream.empty();
			//#endif
		}

		// vanilla
		return world.getBlockCollisions(entity, box);
	}

	//#if MC >= 11800
	//$$ @Redirect(
	//$$ 		method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Ljava/util/List;isEmpty()Z",
	//$$ 				ordinal = 0
	//$$ 		)
	//$$ )
	//$$ private static boolean theCollisionsListParameterIsIncompleteSoDontReturnEvenIfItIsEmpty(List<VoxelShape> voxelShapeList)
	//$$ {
	//$$ 	if (ofemContext.get() != null)
	//$$ 	{
	//$$ 		return false;
	//$$ 	}
	//$$ 	return voxelShapeList.isEmpty();
	//$$ }
	//#endif

	@Redirect(
			//#if MC >= 11800
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11600
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/collection/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11500
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#else
			//$$ method = "method_20737",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D"
					//#else
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/util/stream/Stream;D)D"
					//#endif
			),
			require = 4
	)
	private static double useAxisOnlyBlockCollisions(
			Direction.Axis axis, Box box,

			//#if MC >= 11800
			//$$ Iterable<VoxelShape> shapes,
			//#else
			Stream<VoxelShape> shapes,
			//#endif

			double maxDist,
			/* parent method parameters vvv */

			Vec3d movement, Box entityBoundingBox,

			//#if MC >= 11800
			//$$ List<VoxelShape> collisionsExceptBlockCollisions
			//#else
			ReusableStream<VoxelShape> collisions
			//#endif
	)
	{
		OFEMContext ctx = ofemContext.get();
		if (ctx != null)
		{
			ctx.axis = axis;
			ctx.movementOnAxis = maxDist;
			ctx.entityBoundingBox = entityBoundingBox;

			//#if MC >= 11800
			//$$ Iterable<VoxelShape>
			//#else
			Stream<VoxelShape>
			//#endif
					blockCollisions = OFEMUtil.getAxisOnlyBlockCollision(ctx);

			// order: (entity, border), block
			//#if MC >= 11800
			//$$ List<VoxelShape> voxelShapeList = Lists.newArrayList();
			//$$ collisionsExceptBlockCollisions.forEach(voxelShapeList::add);
			//$$ blockCollisions.forEach(voxelShapeList::add);
			//$$ shapes = voxelShapeList;
			//#else
			shapes = Stream.concat(collisions.stream(), blockCollisions);
			//#endif
		}
		return VoxelShapes.calculateMaxOffset(axis, box, shapes, maxDist);
	}
}
