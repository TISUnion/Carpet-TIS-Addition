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

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.FormattedTextValue;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.ModIds;
import com.google.gson.JsonParseException;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12005
//$$ import net.minecraft.registry.DynamicRegistryManager;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(ShapeDispatcher.FormattedTextParam.class)
public abstract class FormattedTextParamMixin
{
	/**
	 * Added try-catch in case it deserialize failed
	 * Might happen e.g. when client receive text tag from 1.15.2 server with old fabric carpet, since the content would
	 * be a raw string instead of a serialized text string
	 */
	@WrapOperation(
			method = "decode",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12005
					//$$ target = "Lcarpet/script/value/FormattedTextValue;deserialize(Ljava/lang/String;Lnet/minecraft/registry/DynamicRegistryManager;)Lcarpet/script/value/FormattedTextValue;"
					//#else
					target = "Lcarpet/script/value/FormattedTextValue;deserialize(Ljava/lang/String;)Lcarpet/script/value/FormattedTextValue;",
					remap = false
					//#endif
			)
	)
	public FormattedTextValue makeItFailsafe(
			String str,
			//#if MC >= 12005
			//$$ DynamicRegistryManager regs,
			//#endif
			Operation<FormattedTextValue> original
	)
	{
		try
		{
			return original.call(
					str
					//#if MC >= 12005
					//$$ , regs
					//#endif
			);
		}
		catch (JsonParseException e)
		{
			CarpetTISAdditionServer.LOGGER.warn("Fail to decode incoming tag in FormattedTextParam, text \"{}\" is not deserialize-able", str);
			return new FormattedTextValue(Messenger.s(str));
		}
	}
}