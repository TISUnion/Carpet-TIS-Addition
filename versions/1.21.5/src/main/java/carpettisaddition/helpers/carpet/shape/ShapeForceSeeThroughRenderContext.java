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

/**
 *            mc <  1.21.5 : subproject 1.15.2 (main project)
 *  1.21.5 <= mc <  1.21.11: subproject 1.21.5                 <--------
 * 1.21.11 <= mc <  26.2   : subproject 1.21.11
 *            mc >= 26.2   : subproject 26.2
 */
public class ShapeForceSeeThroughRenderContext
{
	// Static geometry helpers need the current shape's render state.
	private static final ThreadLocal<Boolean> FORCE_SEE_THROUGH = new ThreadLocal<>();

	public static boolean isForceSeeThrough()
	{
		return Boolean.TRUE.equals(FORCE_SEE_THROUGH.get());
	}

	public static boolean setForceSeeThrough(boolean forceSeeThrough)
	{
		boolean previous = ShapeForceSeeThroughRenderContext.isForceSeeThrough();
		if (forceSeeThrough)
		{
			FORCE_SEE_THROUGH.set(true);
		}
		else
		{
			FORCE_SEE_THROUGH.remove();
		}
		return previous;
	}
}
