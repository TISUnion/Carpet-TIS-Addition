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

package carpettisaddition.mixins.carpet.tweaks.robustness;

import carpet.script.exception.InternalExpressionException;
import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.FormattedTextValue;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.Tag;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapeDispatcher.FormattedTextParam.class)
public abstract class FormattedTextParamMixin
{
	/**
	 * Added try-catch in case it deserialize failed
	 * Might happen e.g. when client receive text tag from a MC server in old version.
	 * <p>
	 * Data formats in different versions:
	 * - (~, 1.16)      : string, as a literal text value
	 * - [1.16, 1.21.5) : string, as a serialized text json
	 * - [1.21.5, ~)    : nbt element, as a serialized text nbt        <--------
	 */
	@WrapOperation(
			method = "decode",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/script/value/FormattedTextValue;deserialize(Lnet/minecraft/nbt/NbtElement;Lnet/minecraft/registry/DynamicRegistryManager;)Lcarpet/script/value/FormattedTextValue;"
			)
	)
	public FormattedTextValue makeItFailsafe(Tag nbtElement, RegistryAccess regs, Operation<FormattedTextValue> original)
	{
		try
		{
			return original.call(nbtElement, regs);
		}
		catch (InternalExpressionException e)
		{
			String str = nbtElement.toString();
			CarpetTISAdditionServer.LOGGER.warn("Fail to decode incoming tag in FormattedTextParam, text \"{}\" is not deserialize-able", str);
			return new FormattedTextValue(Messenger.s(str));
		}
	}
}