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

	public static boolean checkMovement(Vec3d movement)
	{
		return movement.lengthSquared() >= OPTIMIZE_THRESHOLD * OPTIMIZE_THRESHOLD || CarpetTISAdditionSettings.ultraSecretSetting.equals("optimizedFastEntityMovement");
	}

	public static OFEMContext createContext(World world, Entity entity)
	{
		return new OFEMContext(world, entity);
	}

	@Nullable
	public static OFEMContext checkAndCreateContext(World world, Entity entity, Vec3d movement)
	{
		if (CarpetTISAdditionSettings.optimizedFastEntityMovement && checkMovement(movement))
		{
			return createContext(world, entity);
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