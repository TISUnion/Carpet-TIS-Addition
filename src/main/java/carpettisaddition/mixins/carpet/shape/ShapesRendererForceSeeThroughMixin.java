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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 1.17
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if MC < 1.21
import com.mojang.blaze3d.vertex.BufferBuilder;
//#endif

/**
 *             mc <  1.15   : subproject 1.14.4
 *     1.15 <= mc <  1.21.5 : subproject 1.15.2 (main project)  <--------
 *   1.21.5 <= mc <  1.21.11: subproject 1.21.5
 *  1.21.11 <= mc <  26.2   : subproject 1.21.11
 *             mc >= 26.2   : subproject 26.2
 */
@Mixin(ShapesRenderer.class)
public abstract class ShapesRendererForceSeeThroughMixin
{
	@WrapOperation(
			//#if MC >= 1.17
			//$$ method = {"lambda$render$1", "lambda$render$4"},
			//#else
			method = {"lambda$render$2", "lambda$render$3"},
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21
					//$$ target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/Tesselator;DDDF)V"
					//#elseif MC >= 1.17
					//$$ target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/Tesselator;Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDF)V"
					//#else
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderLines(Lcom/mojang/blaze3d/vertex/Tesselator;Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDF)V"
					//#endif
			)
	)
	private static void forceSeeThrough_disableDepthForLines(
			ShapesRenderer.RenderedShape<?> shape,
			//#if MC >= 1.17
			//$$ PoseStack matrices,
			//#endif
			Tesselator tessellator,
			//#if MC < 1.21
			BufferBuilder builder,
			//#endif
			double cameraX, double cameraY, double cameraZ, float partialTick, Operation<Void> original
	)
	{
		ShapesRendererForceSeeThroughMixin.renderForceSeeThrough(shape, () -> original.call(shape,
				//#if MC >= 1.17
				//$$ matrices,
				//#endif
				tessellator,
				//#if MC < 1.21
				builder,
				//#endif
				cameraX, cameraY, cameraZ, partialTick));
	}

	@WrapOperation(
			//#if MC >= 1.17
			//$$ method = "lambda$render$2",
			//#else
			method = "lambda$render$1",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21
					//$$ target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderFaces(Lcom/mojang/blaze3d/vertex/Tesselator;DDDF)V"
					//#else
					target = "Lcarpet/script/utils/ShapesRenderer$RenderedShape;renderFaces(Lcom/mojang/blaze3d/vertex/Tesselator;Lcom/mojang/blaze3d/vertex/BufferBuilder;DDDF)V"
					//#endif
			)
	)
	private static void forceSeeThrough_disableDepthForFaces(
			ShapesRenderer.RenderedShape<?> shape, Tesselator tessellator,
			//#if MC < 1.21
			BufferBuilder builder,
			//#endif
			double cameraX, double cameraY, double cameraZ, float partialTick, Operation<Void> original
	)
	{
		renderForceSeeThrough(shape, () -> original.call(shape, tessellator,
				//#if MC < 1.21
				builder,
				//#endif
				cameraX, cameraY, cameraZ, partialTick));
	}

	@Unique
	private static void renderForceSeeThrough(ShapesRenderer.RenderedShape<?> renderedShape, Runnable render)
	{
		//#if MC >= 1.19.4
		//$$ if (renderedShape instanceof ShapesRenderer.RenderedSprite)
		//$$ {
		//$$ 	render.run();
		//$$ 	return;
		//$$ }
		//#endif
		ShapeDispatcherForceSeeThroughExpiringShape shape = (ShapeDispatcherForceSeeThroughExpiringShape)((ShapesRendererRenderedShapeAccessor)renderedShape).getShape$TISCM();
		if (!shape.isForceSeeThrough$TISCM())
		{
			render.run();
			return;
		}

		boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
		boolean writeDepth = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		try
		{
			render.run();
		}
		finally
		{
			RenderSystem.depthMask(writeDepth);
			if (depthTest)
			{
				RenderSystem.enableDepthTest();
			}
			else
			{
				RenderSystem.disableDepthTest();
			}
		}
	}
}
