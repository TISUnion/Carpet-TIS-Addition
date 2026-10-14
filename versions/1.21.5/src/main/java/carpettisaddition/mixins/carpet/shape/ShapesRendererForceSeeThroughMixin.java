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

import carpet.script.utils.ShapesRenderer;
import carpettisaddition.helpers.carpet.shape.ShapeDispatcherForceSeeThroughExpiringShape;
import carpettisaddition.helpers.carpet.shape.ShapeForceSeeThroughRenderContext;
import carpettisaddition.helpers.carpet.shape.ShapeForceSeeThroughRenderTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 1.21.10
//$$ import net.minecraft.client.renderer.state.LevelRenderState;
//#endif

/**
 *             mc <  1.15   : subproject 1.14.4
 *     1.15 <= mc <  1.21.5 : subproject 1.15.2 (main project)
 *   1.21.5 <= mc <  1.21.11: subproject 1.21.5                 <--------
 *  1.21.11 <= mc <  26.2   : subproject 1.21.11
 *             mc >= 26.2   : subproject 26.2
 *
 * RenderPipeline applies depth state during each mesh draw.
 */
@Mixin(ShapesRenderer.class)
public abstract class ShapesRendererForceSeeThroughMixin
{
	@Unique
	private static boolean isForceSeeThrough(ShapesRenderer.RenderedShape<?> renderedShape)
	{
		return ((ShapeDispatcherForceSeeThroughExpiringShape)((ShapesRendererRenderedShapeAccessor)renderedShape).getShape$TISCM()).isForceSeeThrough$TISCM();
	}

	@WrapOperation(
			method = {"lambda$render$1", "lambda$render$4"},
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.10
					//$$ target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/Tesselator;DDDFLnet/minecraft/client/renderer/state/LevelRenderState;)V",
					//#else
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/Tesselator;DDDF)V",
					//#endif
					remap = true
			),
			remap = false
	)
	private static void forceSeeThrough_setContextForLines(
			ShapesRenderer.RenderedShape<?> renderedShape, PoseStack matrices, Tesselator tesselator, double cx, double cy, double cz, float partialTick,
			//#if MC >= 1.21.10
			//$$ LevelRenderState levelRenderState,
			//#endif
			Operation<Void> original
	)
	{
		boolean previous = ShapeForceSeeThroughRenderContext.setForceSeeThrough(ShapesRendererForceSeeThroughMixin.isForceSeeThrough(renderedShape));
		try
		{
			original.call(renderedShape, matrices, tesselator, cx, cy, cz, partialTick
					//#if MC >= 1.21.10
					//$$ , levelRenderState
					//#endif
			);
		}
		finally
		{
			ShapeForceSeeThroughRenderContext.setForceSeeThrough(previous);
		}
	}

	@WrapOperation(
			method = "lambda$render$2",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderFaces(Lcom/mojang/blaze3d/vertex/Tesselator;DDDF)V",
					remap = true
			),
			remap = false
	)
	private static void forceSeeThrough_setContextForFaces(
			ShapesRenderer.RenderedShape<?> renderedShape, Tesselator tesselator, double cx, double cy, double cz, float partialTick, Operation<Void> original
	)
	{
		boolean previous = ShapeForceSeeThroughRenderContext.setForceSeeThrough(ShapesRendererForceSeeThroughMixin.isForceSeeThrough(renderedShape));
		try
		{
			original.call(renderedShape, tesselator, cx, cy, cz, partialTick);
		}
		finally
		{
			ShapeForceSeeThroughRenderContext.setForceSeeThrough(previous);
		}
	}

	@WrapOperation(
			method = "drawWithShader",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;draw(Lcom/mojang/blaze3d/vertex/MeshData;)V", remap = true),
			remap = false
	)
	private static void forceSeeThrough_replaceRenderType(RenderType renderType, MeshData mesh, Operation<Void> original)
	{
		original.call(ShapeForceSeeThroughRenderContext.isForceSeeThrough() ? ShapeForceSeeThroughRenderTypes.get(renderType) : renderType, mesh);
	}
}
