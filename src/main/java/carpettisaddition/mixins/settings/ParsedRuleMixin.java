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

package carpettisaddition.mixins.settings;

//#if MC >= 11901
//$$ import carpet.api.settings.InvalidRuleValueException;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

import carpet.settings.Validator;
import carpettisaddition.settings.validator.AbstractValidator;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

//#if MC >= 11901
//$$ @SuppressWarnings({"deprecation", "removal"})
//#endif
@Mixin(
		// dont remap to CarpetRule
		//#disable-remap
		carpet.settings.ParsedRule.class
		//#enable-remap
)
public class ParsedRuleMixin<T>
{
	@Inject(
			//#if MC >= 11901
			//$$ method = "set(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/Object;Ljava/lang/String;)V",
			//#else
			method = "set(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/Object;Ljava/lang/String;)Lcarpet/settings/ParsedRule;",
			//#endif
			at = @At(
					value = "INVOKE_ASSIGN",
					//#if MC >= 11901
					//$$ target = "Lcarpet/api/settings/Validator;validate(Lnet/minecraft/server/command/ServerCommandSource;Lcarpet/api/settings/CarpetRule;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
					//#else
					target = "Lcarpet/settings/Validator;validate(Lnet/minecraft/commands/CommandSourceStack;Lcarpet/settings/ParsedRule;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
					//#endif
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
			//#if MC < 11901
			, cancellable = true
			//#endif
	)
	private void TISCMValidatorFailureLogicOverride(
			CommandSourceStack source, T value, String stringValue,
			//#if MC >= 11901
			//$$ CallbackInfo ci,
			//#else
			CallbackInfoReturnable<?> cir,
			//#endif
			Iterator<?> iterator, Validator<T> validator
	)
	//#if MC >= 11901
	//$$ throws InvalidRuleValueException
	//#endif
	{
		if (value == null)
		{
			if (validator instanceof AbstractValidator)
			{
				//#if MC >= 11901
				//$$ throw new InvalidRuleValueException();
				//#else
				cir.setReturnValue(null);
				//#endif
			}
		}
	}
}
