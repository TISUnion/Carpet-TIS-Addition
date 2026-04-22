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

package carpettisaddition.mixins.carpet.shape.texthack;

import carpettisaddition.helpers.carpet.shape.IShapesRendererRenderedTextGlyphVisitor;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=26.2"))
@Mixin(targets = "carpet.script.utils.ShapesRenderer$RenderedText$1")
public abstract class ShapesRendererRenderedTextGlyphVisitorMixin implements IShapesRendererRenderedTextGlyphVisitor
{
	@Unique
	private boolean isMicroTimingMarkerText$TISCM = false;

	@Override
	public void setIsMicroTimingMarkerText$TISCM()
	{
		this.isMicroTimingMarkerText$TISCM = true;
	}

	@ModifyArg(
			method = "acceptRenderable",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/font/TextRenderable;renderType(Lnet/minecraft/client/gui/Font$DisplayMode;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
			)
	)
	private Font.DisplayMode seeThroughWhenNecessary(Font.DisplayMode value)
	{
		return this.isMicroTimingMarkerText$TISCM ? Font.DisplayMode.SEE_THROUGH : value;
	}
}
