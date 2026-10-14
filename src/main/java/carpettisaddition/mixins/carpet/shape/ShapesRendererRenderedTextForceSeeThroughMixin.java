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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 1.19.4
//$$ import net.minecraft.client.gui.Font;
//#endif

/**
 *         mc <  1.15  : subproject 1.14.4
 * 1.15 <= mc <  1.21.5: subproject 1.15.2 (main project)  <--------
 *         mc >= 1.21.5: subproject 1.21.5
 */
@Mixin(ShapesRenderer.RenderedText.class)
public abstract class ShapesRendererRenderedTextForceSeeThroughMixin
{
	@ModifyArg(
			method = "renderLines",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.19.4
					//$$ target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
					//#elseif MC >= 1.16
					//$$ target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I",
					//#else
					target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I",
					//#endif
					remap = true
			),
			index = 7,
			remap = false
	)
	private
	//#if MC >= 1.19.4
	//$$ Font.DisplayMode
	//#else
	boolean
	//#endif
	forceSeeThrough_setTextDisplayMode(
			//#if MC >= 1.19.4
			//$$ Font.DisplayMode original
			//#else
			boolean original
			//#endif
	)
	{
		ShapeDispatcherForceSeeThroughExpiringShape shape = (ShapeDispatcherForceSeeThroughExpiringShape)((ShapesRendererRenderedShapeAccessor)this).getShape$TISCM();
		return shape.isForceSeeThrough$TISCM() ?
				//#if MC >= 1.19.4
				//$$ Font.DisplayMode.SEE_THROUGH :
				//#else
				true :
				//#endif
				original;
	}
}
