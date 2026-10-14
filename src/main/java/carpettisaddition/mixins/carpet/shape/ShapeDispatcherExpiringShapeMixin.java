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

package carpettisaddition.mixins.carpet.shape;

import carpet.script.utils.ShapeDispatcher;
import carpettisaddition.helpers.carpet.shape.ShapeDispatcherForceSeeThroughExpiringShape;
import carpettisaddition.helpers.carpet.shape.ShapeDispatcherPreciseExpiringShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * mc <  1.15: subproject 1.14.4
 * mc >= 1.15: subproject 1.15.2 (main project)  <--------
 */
@Mixin(ShapeDispatcher.ExpiringShape.class)
public abstract class ShapeDispatcherExpiringShapeMixin implements ShapeDispatcherForceSeeThroughExpiringShape, ShapeDispatcherPreciseExpiringShape
{
	@Unique
	private boolean forceSeeThrough$TISCM;

	@Unique
	private boolean precise$TISCM;

	@Override
	public boolean isForceSeeThrough$TISCM()
	{
		return this.forceSeeThrough$TISCM;
	}

	@Override
	public void setForceSeeThrough$TISCM(boolean forceSeeThrough)
	{
		this.forceSeeThrough$TISCM = forceSeeThrough;
	}

	@Override
	public boolean isPrecise$TISCM()
	{
		return this.precise$TISCM;
	}

	@Override
	public void setPrecise$TISCM(boolean precise)
	{
		this.precise$TISCM = precise;
	}
}
