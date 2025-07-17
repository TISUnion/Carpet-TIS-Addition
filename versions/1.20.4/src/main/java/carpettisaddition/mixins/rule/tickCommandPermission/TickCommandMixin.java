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

package carpettisaddition.mixins.rule.tickCommandPermission;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
import carpettisaddition.utils.CarpetModUtil;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TickCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * mc1.14   ~ mc1.20.3: subproject 1.15.2 (main project)
 * mc1.20.4 ~ mc1.21.5: subproject 1.20.4        <--------
 * mc1.21.6+          : subproject 1.21.8
 */
@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	/**
	 * the lambda in the {@link com.mojang.brigadier.builder.ArgumentBuilder#requires} methods
	 * for the node builder in the {@link TickCommand#register} method
	 */
	@Inject(method = "method_54709", at = @At("HEAD"), cancellable = true)
	private static void overrideTickCommandPermission(ServerCommandSource source, CallbackInfoReturnable<Boolean> cir)
	{
		var ruleValue = TickCommandCarpetfiedRules.tickCommandPermission();
		if (!CarpetTISAdditionSettings.VANILLA_TICK_COMMAND_PERMISSION.equals(ruleValue))
		{
			cir.setReturnValue(CarpetModUtil.canUseCommand(source, ruleValue));
		}
	}
}
