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
import carpet.script.value.Value;
import carpettisaddition.helpers.carpet.shape.IShapeDispatcherText;
import carpettisaddition.helpers.carpet.shape.ScarpetDisplayedTextHack;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

//#if MC >= 11903
//$$ import org.spongepowered.asm.mixin.injection.Coerce;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"))
@Mixin(ShapeDispatcher.Text.class)
public abstract class ShapeDispatcherTextMixin implements IShapeDispatcherText
{
	private boolean isMicroTimingMarkerText = false;

	@Override
	public boolean isMicroTimingMarkerText()
	{
		return this.isMicroTimingMarkerText;
	}

	@Inject(
			method = "init",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=align",
					ordinal = 0
			),
			remap = false
	)
	private void checkScarpetDisplayedTextHack(
			Map<String, Value> options,
			//#if MC >= 11903
			//$$ @Coerce Object regs,
			//#endif
			CallbackInfo ci
	)
	{
		if (options.containsKey("align"))
		{
			String alignStr = options.get("align").getString();
			if (ScarpetDisplayedTextHack.MICRO_TIMING_TEXT_MAGIC_STRING.equals(alignStr))
			{
				this.isMicroTimingMarkerText = true;
			}
		}
	}
}
