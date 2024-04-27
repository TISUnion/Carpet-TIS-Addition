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

package carpettisaddition.mixins.rule.failSoftBlockStateParsing;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.failSoftBlockStateParsing.DummyPropertyEnum;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// improves priority due to @ModifyVariable into locals
@Mixin(value = BlockArgumentParser.class, priority = 500)
public abstract class BlockArgumentParserMixin
{
	@Inject(
			method = "parsePropertyValue",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/StringReader;setCursor(I)V",
					remap = false
			),
			cancellable = true
	)
	private void failSoftBlockStateParsing(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.failSoftBlockStateParsing)
		{
			ci.cancel();
		}
	}

	/**
	 * Replace the property field with our DUMMY_PROPERTY so the following parsing is able to continue
	 * DUMMY_PROPERTY has no possible values (since it's an enum property with 0 enum value) so it doesn't provide
	 * any suggestion, and it will fail in BlockArgumentParser#parsePropertyValue which will be suppressed
	 * in our @Inject above
	 */
	@ModifyVariable(
			method = "parseBlockProperties",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/state/StateManager;getProperty(Ljava/lang/String;)Lnet/minecraft/state/property/Property;"
			),
			index = 3
	)
	private Property<?> failSoftBlockStateParsing(Property<?> value)
	{
		if (CarpetTISAdditionSettings.failSoftBlockStateParsing)
		{
			if (value == null)
			{
				value = DummyPropertyEnum.DUMMY_PROPERTY;
			}
		}
		return value;
	}
}
