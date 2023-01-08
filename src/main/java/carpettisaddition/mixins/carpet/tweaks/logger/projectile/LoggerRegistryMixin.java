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

package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpet.logging.LoggerRegistry;
import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;

@Mixin(LoggerRegistry.class)
public abstract class LoggerRegistryMixin
{
	@ModifyArg(
			//#if MC >= 11500
			method = "registerLoggers",
			//#else
			//$$ method = "initLoggers",
			//#endif
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=projectiles",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lcarpet/logging/Logger;stardardLogger(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Lcarpet/logging/Logger;",
					//#else
					//$$ target = "Lcarpet/logging/Logger;<init>(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V",
					//#endif
					ordinal = 0,
					remap = false
			),
			index = 2,
			remap = false
	)
	private static String[] modifyProjectileLoggerOptions(String[] options)
	{
		List<String> optionList = Lists.newArrayList(options);
		optionList.add("visualize");
		return optionList.toArray(new String[0]);
	}
}
