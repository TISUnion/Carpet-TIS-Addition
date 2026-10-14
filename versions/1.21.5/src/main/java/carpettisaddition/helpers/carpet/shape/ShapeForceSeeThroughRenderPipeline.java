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

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;

/**
 *           mc <  1.21.5: subproject 1.15.2 (main project)
 * 1.21.5 <= mc <  26.1.2: subproject 1.21.5                 <--------
 * 26.1.2 <= mc <  26.2  : subproject 26.1.2
 *           mc >= 26.2  : subproject 26.2
 *
 * Preserves the original pipeline settings except depth access.
 */
public final class ShapeForceSeeThroughRenderPipeline extends RenderPipeline
{
	ShapeForceSeeThroughRenderPipeline(RenderPipeline source)
	{
		super(
				source.getLocation().withSuffix("_tiscm_force_see_through"),
				source.getVertexShader(), source.getFragmentShader(), source.getShaderDefines(), source.getSamplers(), source.getUniforms(),
				source.getBlendFunction(), DepthTestFunction.NO_DEPTH_TEST, source.getPolygonMode(), source.isCull(),
				source.isWriteColor(), source.isWriteAlpha(), false, source.getColorLogic(), source.getVertexFormat(), source.getVertexFormatMode(), 0, 0
				//#if MC >= 1.21.8
				//$$ , source.getSortKey()
				//#endif
		);
	}
}
