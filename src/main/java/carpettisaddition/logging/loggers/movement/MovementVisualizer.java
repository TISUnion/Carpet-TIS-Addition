/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.logging.loggers.movement;

import carpet.script.value.NumericValue;
import carpettisaddition.helpers.carpet.shape.ShapeHolder;
import carpettisaddition.helpers.carpet.shape.ShapeUtil;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

class MovementVisualizer
{
	private static final int DURATION = 100;
	private static final double POSITION_TOLERANCE = 1e-12;
	private static final int POSITION_ROUNDING_ULPS = 4;
	private static final double MAX_ARROW_LENGTH = 0.1;
	private static final float LINE_WIDTH = 2.0F;
	private static final float MOVEMENT_LINE_WIDTH = 3.0F;
	private static final long CANDIDATE_COLOR = 0xFF55FFFFL;
	private static final long CALCULATED_COLOR = 0xFFFF00FFL;
	private static final long END_COLOR = 0x0000FFFFL;

	private final DimensionWrapper dimension;
	private final long movementColor;
	private final List<ShapeHolder<?>> shapes = Lists.newArrayList();

	private MovementVisualizer(DimensionWrapper dimension, MoverType movementType)
	{
		this.dimension = dimension;
		this.movementColor = MovementVisualizer.getMovementColor(movementType);
	}

	public static List<ShapeHolder<?>> createShapes(Vec3 startPos, AABB startBox, List<Vec3> movements, Vec3 endPos, AABB endBox, DimensionWrapper dimension, MoverType movementType)
	{
		MovementVisualizer visualizer = new MovementVisualizer(dimension, movementType);
		Vec3 tolerance = new Vec3(
				MovementVisualizer.getCoordinateTolerance(startPos.x, endPos.x, startBox.minX, startBox.maxX, endBox.minX, endBox.maxX),
				MovementVisualizer.getCoordinateTolerance(startPos.y, endPos.y, startBox.minY, startBox.maxY, endBox.minY, endBox.maxY),
				MovementVisualizer.getCoordinateTolerance(startPos.z, endPos.z, startBox.minZ, startBox.maxZ, endBox.minZ, endBox.maxZ)
		);
		for (int i = 0; i < movements.size(); i++)
		{
			Vec3 movement = movements.get(i);
			Vec3 candidatePos = startPos.add(movement);
			if (MovementVisualizer.isSamePosition(candidatePos, endPos, tolerance) || movement.lengthSqr() == 0)
			{
				continue;
			}
			// All candidates start at the original position; they are not successive path segments.
			long color = i == movements.size() - 1 ? MovementVisualizer.CALCULATED_COLOR : MovementVisualizer.CANDIDATE_COLOR;
			visualizer.drawBox(startBox.move(movement), color);
			visualizer.drawLine(startPos, candidatePos, color, MovementVisualizer.LINE_WIDTH);
		}
		visualizer.drawArrow(startPos, endPos);
		visualizer.drawBox(endBox, MovementVisualizer.END_COLOR);
		return visualizer.shapes;
	}

	private void drawArrow(Vec3 start, Vec3 end)
	{
		Vec3 movement = end.subtract(start);
		double length = Math.sqrt(movement.lengthSqr());
		if (length == 0)
		{
			return;
		}
		this.drawLine(start, end, this.movementColor, MovementVisualizer.MOVEMENT_LINE_WIDTH);
		// Vec3.normalize() discards very short vectors in older versions.
		Vec3 direction = movement.scale(1.0 / length);
		Vec3 reference = Math.abs(direction.y) < 0.9 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
		double arrowLength = Math.min(length * 0.25, MovementVisualizer.MAX_ARROW_LENGTH);
		Vec3 side = direction.cross(reference).normalize().scale(arrowLength * 0.5);
		Vec3 otherSide = direction.cross(side);
		Vec3 base = end.subtract(direction.scale(arrowLength));
		// Two perpendicular V shapes keep the arrow visible from different viewing angles.
		this.drawLine(base.add(side), end, this.movementColor, MovementVisualizer.MOVEMENT_LINE_WIDTH);
		this.drawLine(base.subtract(side), end, this.movementColor, MovementVisualizer.MOVEMENT_LINE_WIDTH);
		this.drawLine(base.add(otherSide), end, this.movementColor, MovementVisualizer.MOVEMENT_LINE_WIDTH);
		this.drawLine(base.subtract(otherSide), end, this.movementColor, MovementVisualizer.MOVEMENT_LINE_WIDTH);
	}

	private void drawBox(AABB box, long color)
	{
		this.addShape(ShapeUtil.createBox(new Vec3(box.minX, box.minY, box.minZ), new Vec3(box.maxX, box.maxY, box.maxZ), this.dimension, color));
	}

	private void drawLine(Vec3 start, Vec3 end, long color, float lineWidth)
	{
		if (!start.equals(end))
		{
			ShapeHolder<?> shape = ShapeUtil.createLine(start, end, this.dimension, color);
			shape.setValue("line", new NumericValue(lineWidth));
			this.addShape(shape);
		}
	}

	private static double getCoordinateTolerance(double start, double end, double startMin, double startMax, double endMin, double endMax)
	{
		double magnitude = Math.max(Math.abs(start), Math.abs(end));
		magnitude = Math.max(magnitude, Math.max(Math.abs(startMin), Math.abs(startMax)));
		magnitude = Math.max(magnitude, Math.max(Math.abs(endMin), Math.abs(endMax)));
		// Account for rounding when the final position is reconstructed from the bounding box.
		return Math.max(MovementVisualizer.POSITION_TOLERANCE, Math.ulp(magnitude) * MovementVisualizer.POSITION_ROUNDING_ULPS);
	}

	private static long getMovementColor(MoverType movementType)
	{
		switch (movementType)
		{
			case PLAYER:
				return 0x55FF55FFL;
			case PISTON:
				return 0xFFAA00FFL;
			case SHULKER_BOX:
				return 0xAA88FFFFL;
			case SHULKER:
				return 0x55FFFFFFL;
			case SELF:
			default:
				return 0xFFFFFFFFL;
		}
	}

	private static boolean isSamePosition(Vec3 first, Vec3 second, Vec3 tolerance)
	{
		return Math.abs(first.x - second.x) <= tolerance.x && Math.abs(first.y - second.y) <= tolerance.y && Math.abs(first.z - second.z) <= tolerance.z;
	}

	private void addShape(ShapeHolder<?> shape)
	{
		shape.setDuration(MovementVisualizer.DURATION).setPrecise(true);
		this.shapes.add(shape);
	}
}

