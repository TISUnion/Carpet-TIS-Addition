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

package carpettisaddition.mixins.utils.command;

import carpettisaddition.utils.command.CommandSourceStackWithExtraContextArguments;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandContext.class)
public abstract class CommandContextMixin<S>
{
	@Shadow(remap = false) @Final
	private S source;

	@ModifyExpressionValue(
			method = "getArgument",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
			),
			remap = false
	)
	private Object getArgumentFromExtra$TISCM(Object value, @Local(argsOnly = true) String name)
	{
		if (value == null && this.source instanceof CommandSourceStackWithExtraContextArguments)
		{
			value = ((CommandSourceStackWithExtraContextArguments)this.source).getExtraContextArgument$TISCM(name);
		}
		return value;
	}
}
