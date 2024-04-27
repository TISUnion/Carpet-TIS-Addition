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

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMContext;
import carpettisaddition.helpers.rule.optimizedFastEntityMovement.OFEMUtil;
import carpettisaddition.utils.mixin.testers.LithiumEntityMovementOptimizationTester;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

//#if MC >= 11800
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Collections;
import java.util.List;
//#else
//$$ import java.util.stream.Stream;
//#endif

@Restriction(conflict = @Condition(type = Condition.Type.TESTER, tester = LithiumEntityMovementOptimizationTester.class))
@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Unique
	private static final ThreadLocal<OFEMContext> ofemContext = ThreadLocal.withInitial(() -> null);

	@WrapOperation(
			//#if MC >= 11800
			method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11600
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Lnet/minecraft/block/ShapeContext;Lnet/minecraft/util/collection/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#else
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityContext;Lnet/minecraft/util/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/lang/Iterable;"
					//#elseif MC >= 11500
					//$$ target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"
					//#else
					//$$ target = "Lnet/minecraft/world/World;method_20812(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"
					//#endif
			)
	)
	private static
	//#if MC >= 11800
	Iterable<VoxelShape>
	//#else
	//$$ Stream<VoxelShape>
	//#endif
	dontUseThatLargeBlockCollisions(
			World world, Entity entity, Box box,
			Operation<
					//#if MC >= 11800
					Iterable<VoxelShape>
					//#else
					//$$ Stream<VoxelShape>
					//#endif
			> original,
			@Local(argsOnly = true) Vec3d movement
	)
	{
		if (CarpetTISAdditionSettings.optimizedFastEntityMovement)
		{
			if (OFEMUtil.checkMovement(movement))
			{
				ofemContext.set(OFEMUtil.createContext(world, entity));
				//#if MC >= 11800
				return Collections.emptyList();
				//#else
				//$$ return Stream.empty();
				//#endif
			}
		}
		ofemContext.remove();

		// vanilla
		return original.call(world, entity, box);
	}

	//#if MC >= 11800
	@ModifyExpressionValue(
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					ordinal = 0
			)
	)
	private static boolean theCollisionsListParameterIsIncompleteSoDontReturnEvenIfItIsEmpty(boolean isEmpty)
	{
		if (ofemContext.get() != null)
		{
			isEmpty = false;
		}
		return isEmpty;
	}
	//#endif

	@ModifyArgs(
			//#if MC >= 11800
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11600
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/collection/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#elseif MC >= 11500
			//$$ method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/ReusableStream;)Lnet/minecraft/util/math/Vec3d;",
			//#else
			//$$ method = "method_20737",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D"
					//#else
					//$$ target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/util/stream/Stream;D)D"
					//#endif
			),
			require = 4
	)
	private static void useAxisOnlyBlockCollisions(Args args)
	{
		OFEMContext ctx = ofemContext.get();
		if (ctx != null)
		{
			// Direction.Axis axis, Box box, (Iterable<VoxelShape> | Stream<VoxelShape>) shapes, double maxDist
			Direction.Axis axis = args.get(0);
			Box entityBoundingBox = args.get(1);
			//#if MC >= 11800
			List<VoxelShape>
			//#else
			//$$ Stream<VoxelShape>
			//#endif
					entityAndBorderCollisions = args.get(2);
			double maxDist = args.get(3);

			ctx.axis = axis;
			ctx.movementOnAxis = maxDist;
			ctx.entityBoundingBox = entityBoundingBox;

			//#if MC >= 11800
			Iterable<VoxelShape>
			//#else
			//$$ Stream<VoxelShape>
			//#endif
					blockCollisions = OFEMUtil.getAxisOnlyBlockCollision(ctx);

			// order: (entity, border), block
			//#if MC >= 11800
			List<VoxelShape> voxelShapeList = Lists.newArrayList();
			voxelShapeList.addAll(entityAndBorderCollisions);
			blockCollisions.forEach(voxelShapeList::add);
			args.set(2, voxelShapeList);
			//#else
			//$$ args.set(2, Stream.concat(entityAndBorderCollisions, blockCollisions));
			//#endif
		}
	}
}
