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
import carpet.script.utils.ShapesRenderer;
import carpettisaddition.helpers.carpet.shape.ShapeDispatcherPreciseExpiringShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc >= 1.15: subproject 1.15.2 (main project)  <--------
 * mc <  1.15: subproject 1.14.4
 */
@Mixin(ShapesRenderer.RenderedShape.class)
public abstract class ShapesRendererRenderedShapeMixin
{
	@Shadow(remap = false) protected ShapeDispatcher.ExpiringShape shape;
	@Shadow(remap = false) private double renderEpsilon;

	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void applyPrecision(CallbackInfo ci)
	{
		if (((ShapeDispatcherPreciseExpiringShape)this.shape).isPrecise$TISCM())
		{
			this.renderEpsilon = 0;
		}
	}

	@Inject(method = "promoteWith", at = @At("TAIL"), remap = false)
	private void updatePrecision(ShapesRenderer.RenderedShape<?> renderedShape, CallbackInfo ci)
	{
		ShapesRendererRenderedShapeAccessor accessor = (ShapesRendererRenderedShapeAccessor)renderedShape;
		((ShapeDispatcherPreciseExpiringShape)this.shape).setPrecise$TISCM(((ShapeDispatcherPreciseExpiringShape)accessor.getShape$TISCM()).isPrecise$TISCM());
		this.renderEpsilon = accessor.getRenderEpsilon$TISCM();
	}
}
