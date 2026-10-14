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

package carpettisaddition.helpers.carpet.shape;

import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import org.jetbrains.annotations.Nullable;

/**
 *            mc <  1.21.5 : subproject 1.15.2 (main project)
 *  1.21.5 <= mc <  1.21.11: subproject 1.21.5
 * 1.21.11 <= mc <  26.2   : subproject 1.21.11                <--------
 *            mc >= 26.2   : subproject 26.2
 */
public class ShapeForceSeeThroughRenderContext
{
	// Static render lambdas share one collector for this render invocation.
	private static final ThreadLocal<DrawableGizmoPrimitives> PRIMITIVES = new ThreadLocal<>();

	public static DrawableGizmoPrimitives getPrimitives()
	{
		DrawableGizmoPrimitives primitives = PRIMITIVES.get();
		if (primitives == null)
		{
			primitives = new DrawableGizmoPrimitives();
			PRIMITIVES.set(primitives);
		}
		return primitives;
	}

	@Nullable
	public static DrawableGizmoPrimitives getPrimitivesIfPresent()
	{
		return PRIMITIVES.get();
	}

	@Nullable
	public static DrawableGizmoPrimitives setPrimitives(@Nullable DrawableGizmoPrimitives primitives)
	{
		DrawableGizmoPrimitives previous = PRIMITIVES.get();
		if (primitives == null)
		{
			PRIMITIVES.remove();
		}
		else
		{
			PRIMITIVES.set(primitives);
		}
		return previous;
	}
}
