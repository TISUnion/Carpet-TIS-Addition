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

package carpettisaddition.helpers.rule.optimizedFastEntityMovement;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class OFEMUtil
{
	private static final double OPTIMIZE_MOVEMENT_THRESHOLD = 10.0D;
	private static final double OPTIMIZE_MOVEMENT_THRESHOLD_SQR = OPTIMIZE_MOVEMENT_THRESHOLD * OPTIMIZE_MOVEMENT_THRESHOLD;

	// overhead due to search box expansion for possible >1m oversized blocks
	private static final double OVERSIZED_OVERHEAD_FACTOR = 12;

	public static boolean checkMovement(@NotNull Vec3 movement)
	{
		boolean movementOk =
				movement.lengthSqr() >= OPTIMIZE_MOVEMENT_THRESHOLD_SQR &&  // basic requirement
				movement.x * movement.y * movement.z > (movement.x + movement.y + movement.z) * OVERSIZED_OVERHEAD_FACTOR;
		return movementOk || CarpetTISAdditionSettings.ultraSecretSetting.equals("optimizedFastEntityMovement");
	}

	public static OFEMContext createContext(Level world, Entity entity, AABB originEntityAabb, Vec3 movement)
	{
		return new OFEMContext(world, entity, originEntityAabb, movement);
	}

	@Nullable
	public static OFEMContext checkAndCreateContext(Level world, Entity entity, AABB originEntityAabb, Vec3 movement)
	{
		if (CarpetTISAdditionSettings.optimizedFastEntityMovement && checkMovement(movement))
		{
			return createContext(world, entity, originEntityAabb, movement);
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
		Vec3 axisOnlyMovement = null;
		switch (context.axis)
		{
			case X:
				axisOnlyMovement = new Vec3(context.movementOnAxis, 0.0D, 0.0D);
				break;
			case Y:
				axisOnlyMovement = new Vec3(0.0D, context.movementOnAxis, 0.0D);
				break;
			case Z:
				axisOnlyMovement = new Vec3(0.0D, 0.0D, context.movementOnAxis);
				break;
		}

		AABB movementBox = context.entityBoundingBox.expandTowards(axisOnlyMovement);

		// Ensure that it doesn't miss those >1m oversized blocks that are still in the vanilla search box
		// Examples:
		// - Downwards moving fence b36 at gt0 (oversized for 1.5m on +y)
		// - Moving piston head with short=false (or just mc < 1.16) at gt0 (oversized for 1.25m on the opposite axis of the move direction)
		// Hopefully there are no >2m +y oversized blocks
		// See also: https://github.com/TISUnion/Carpet-TIS-Addition/issues/267
		AABB searchBox = movementBox.inflate(1, 1, 1).intersect(context.vanillaSearchBox);

		return collisionBoxGetter.get(context.world, context.entity, searchBox);
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
		return getAxisOnlyBlockCollision(context, CollisionGetter::getBlockCollisions);
	}
}