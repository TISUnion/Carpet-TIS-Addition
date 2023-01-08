/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.shape.texthack;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.utils.ShapesRenderer;
import carpettisaddition.helpers.carpet.shape.IShapeDispatcherText;
import carpettisaddition.utils.ModIds;
import com.mojang.blaze3d.systems.RenderSystem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("SimplifiableConditionalExpression")
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"))
@Mixin(ShapesRenderer.RenderedText.class)
public abstract class ShapesRendererRenderedTextMixin<T> extends ShapesRenderer.RenderedShape<ShapeDispatcher.Text>
{
	protected ShapesRendererRenderedTextMixin(MinecraftClient client, ShapeDispatcher.Text shape)
	{
		super(client, shape);
	}

	private boolean isMicroTimingMarkerText()
	{
		return ((IShapeDispatcherText)this.shape).isMicroTimingMarkerText();
	}

	@Inject(
			method = "renderLines",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							//#if MC >= 11600
							//$$ target = "Lcarpet/script/utils/ShapeDispatcher$DisplayedText;indent:F"
							//#else
							target = "Lcarpet/script/utils/ShapeDispatcher$Text;indent:F"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
					//#else
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;scalef(FFF)V",
					//#endif
					ordinal = 0,
					remap = true
			),
			remap = false
	)
	private void doSomeTransparentThing(CallbackInfo ci)
	{
		if (this.isMicroTimingMarkerText())
		{
			RenderSystem.disableDepthTest();
		}
	}

	@ModifyArg(
			method = "renderLines",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11903
					//$$ target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
					//#else
					target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
					//#endif
					remap = true
			),
			index = 7,
			remap = false
	)
	private boolean seeThroughWhenNecessary(boolean value)
	{
		return this.isMicroTimingMarkerText() ? true : value;
	}


	@Inject(
			method = "renderLines",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
					//#else
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;popMatrix()V",
					//#endif
					ordinal = 0,
					remap = true
			),
			remap = false
	)
	private void doSomeCleanUpForTransparentThing(CallbackInfo ci)
	{
		if (this.isMicroTimingMarkerText())
		{
			RenderSystem.enableDepthTest();
		}
	}
}
