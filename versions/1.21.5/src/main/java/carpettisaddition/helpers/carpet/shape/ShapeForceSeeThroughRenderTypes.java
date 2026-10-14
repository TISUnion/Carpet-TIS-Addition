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

import carpettisaddition.mixins.carpet.shape.RenderTypeAccessor;
import carpettisaddition.mixins.carpet.shape.RenderTypeInvoker;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.Map;

/**
 *            mc <  1.21.5 : subproject 1.15.2 (main project)
 *  1.21.5 <= mc <  1.21.11: subproject 1.21.5                 <--------
 * 1.21.11 <= mc <  26.2   : subproject 1.21.11
 *            mc >= 26.2   : subproject 26.2
 */
public final class ShapeForceSeeThroughRenderTypes
{
	private static final Map<RenderType, RenderType> CACHE = Maps.newIdentityHashMap();

	public static RenderType get(RenderType renderType)
	{
		return CACHE.computeIfAbsent(renderType, ShapeForceSeeThroughRenderTypes::create);
	}

	public static MultiBufferSource wrap(MultiBufferSource buffers)
	{
		return renderType -> buffers.getBuffer(ShapeForceSeeThroughRenderTypes.get(renderType));
	}

	private static RenderType create(RenderType source)
	{
		RenderTypeAccessor accessor = (RenderTypeAccessor)source;
		return RenderTypeInvoker.create$TISCM(
				source + "_tiscm_force_see_through", source.bufferSize(), source.affectsCrumbling(), source.sortOnUpload(),
				new ShapeForceSeeThroughRenderPipeline(accessor.getRenderPipeline$TISCM()), accessor.getState$TISCM()
		);
	}
}
