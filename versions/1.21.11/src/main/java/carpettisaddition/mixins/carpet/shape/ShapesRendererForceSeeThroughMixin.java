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
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 26.1
//$$ import net.minecraft.client.renderer.state.level.CameraRenderState;
//$$ import net.minecraft.client.renderer.state.level.LevelRenderState;
//$$ import org.joml.Matrix4fc;
//#else
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.joml.Matrix4f;
//#endif

/**
 *             mc <  1.15   : subproject 1.14.4
 *     1.15 <= mc <  1.21.5 : subproject 1.15.2 (main project)
 *   1.21.5 <= mc <  1.21.11: subproject 1.21.5
 *  1.21.11 <= mc <  26.2   : subproject 1.21.11                <--------
 *             mc >= 26.2   : subproject 26.2
 *
 * Keep forced primitives separate from Carpet's disabled on-top pass.
 */
@Mixin(ShapesRenderer.class)
public abstract class ShapesRendererForceSeeThroughMixin
{
	@Unique
	private static DrawableGizmoPrimitives selectPrimitives(ShapesRenderer.RenderedShape<?> renderedShape, DrawableGizmoPrimitives primitives)
	{
		boolean forceSeeThrough = ((ShapeDispatcherForceSeeThroughExpiringShape)((ShapesRendererRenderedShapeAccessor)renderedShape).getShape$TISCM()).isForceSeeThrough$TISCM();
		return forceSeeThrough ? ShapeForceSeeThroughRenderContext.getPrimitives() : primitives;
	}

	@WrapMethod(method = "render", remap = false)
	private void forceSeeThrough_collectPrimitives(
			RenderBuffers renderBuffers, LevelRenderState levelRenderState,
			//#if MC >= 26.1
			//$$ Matrix4fc modelViewMatrix,
			//#else
			Matrix4f modelViewMatrix,
			//#endif
			float partialTick, Operation<Void> original
	)
	{
		DrawableGizmoPrimitives previous = ShapeForceSeeThroughRenderContext.setPrimitives(null);
		try
		{
			original.call(renderBuffers, levelRenderState, modelViewMatrix, partialTick);
		}
		finally
		{
			ShapeForceSeeThroughRenderContext.setPrimitives(previous);
		}
	}

	@WrapOperation(
			method = {"lambda$render$1", "lambda$render$4"},
			at = @At(
					value = "INVOKE",
					//#if MC >= 26.1
					//$$ target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;DDDFLnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;)V",
					//#else
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;DDDFLnet/minecraft/client/renderer/state/LevelRenderState;Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;)V",
					//#endif
					remap = true
			),
			remap = false
	)
	private static void forceSeeThrough_routeLines(
			ShapesRenderer.RenderedShape<?> renderedShape, PoseStack matrices, double cx, double cy, double cz, float partialTick,
			LevelRenderState levelRenderState, DrawableGizmoPrimitives primitives, Operation<Void> original
	)
	{
		original.call(renderedShape, matrices, cx, cy, cz, partialTick, levelRenderState, ShapesRendererForceSeeThroughMixin.selectPrimitives(renderedShape, primitives));
	}

	@WrapOperation(
			method = "lambda$render$2",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderFaces(Lcom/mojang/blaze3d/vertex/Tesselator;DDDFLnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;)V",
					remap = true
			),
			remap = false
	)
	private static void forceSeeThrough_routeFaces(
			ShapesRenderer.RenderedShape<?> renderedShape, Tesselator tesselator, double cx, double cy, double cz, float partialTick,
			DrawableGizmoPrimitives primitives, Operation<Void> original
	)
	{
		original.call(renderedShape, tesselator, cx, cy, cz, partialTick, ShapesRendererForceSeeThroughMixin.selectPrimitives(renderedShape, primitives));
	}

	@WrapOperation(
			method = "render",
			at = @At(
					value = "INVOKE",
					//#if MC >= 26.1
					//$$ target = "Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;)V",
					//#else
					target = "Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/state/CameraRenderState;Lorg/joml/Matrix4f;)V",
					//#endif
					ordinal = 0,
					remap = true
			),
			remap = false
	)
	private void forceSeeThrough_renderPrimitives(
			DrawableGizmoPrimitives primitives, PoseStack matrices, MultiBufferSource bufferSource, CameraRenderState cameraRenderState,
			//#if MC >= 26.1
			//$$ Matrix4fc modelViewMatrix,
			//#else
			Matrix4f modelViewMatrix,
			//#endif
			Operation<Void> original
	)
	{
		original.call(primitives, matrices, bufferSource, cameraRenderState, modelViewMatrix);
		DrawableGizmoPrimitives forceSeeThroughPrimitives = ShapeForceSeeThroughRenderContext.getPrimitivesIfPresent();
		if (forceSeeThroughPrimitives != null && !forceSeeThroughPrimitives.isEmpty())
		{
			MultiBufferSource.BufferSource buffers = (MultiBufferSource.BufferSource)bufferSource;
			buffers.endLastBatch();
			forceSeeThroughPrimitives.render(matrices, ShapeForceSeeThroughRenderTypes.wrap(bufferSource), cameraRenderState, modelViewMatrix);
			buffers.endLastBatch();
		}
	}
}
