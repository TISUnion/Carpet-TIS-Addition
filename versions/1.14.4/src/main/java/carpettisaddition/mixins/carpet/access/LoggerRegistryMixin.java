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

package carpettisaddition.mixins.carpet.access;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.compat.IExtensionLogger;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Field;
/**
 * for extension logging supports in 1.14.4
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.15"))
@Mixin(LoggerRegistry.class)
public abstract class LoggerRegistryMixin
{
	@Redirect(
			method = "setAccess",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Class;getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;"
			),
			remap = false
	)
	private static Field maybeDontUseYourOwnGetDeclaredField(Class<?> aClass, String name, @Local(argsOnly = true) Logger logger) throws NoSuchFieldException
	{
		if (logger instanceof IExtensionLogger)
		{
			return ((IExtensionLogger)logger).getAcceleratorField();
		}
		return aClass.getDeclaredField(name);
	}
}