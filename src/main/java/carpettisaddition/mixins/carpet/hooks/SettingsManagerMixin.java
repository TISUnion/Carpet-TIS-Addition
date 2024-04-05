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

package carpettisaddition.mixins.carpet.hooks;

import carpet.settings.SettingsManager;
import carpet.utils.Translations;
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpettisaddition.CarpetTISAdditionMod.MOD_NAME;

@Mixin(SettingsManager.class)
public class SettingsManagerMixin
{
	@Inject(
			method = "listAllSettings",

			//#if MC < 11901
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							// after printed fabric-carpet version

							//#if MC >= 11500
							args = "stringValue=ui.version",
							//#else
							//$$ args = "stringValue= version: ",
							//#endif
							ordinal = 0
					)
			),
			//#endif

			at = @At(
					value = "INVOKE",
					//#if MC >= 11901
					//$$ target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;"
					//#elseif MC >= 11600
					//$$ target = "Lcarpet/settings/SettingsManager;getCategories()Ljava/lang/Iterable;",
					//$$ ordinal = 0
					//#else
					target = "Lnet/minecraft/server/command/ServerCommandSource;getPlayer()Lnet/minecraft/server/network/ServerPlayerEntity;",
					ordinal = 0,
					remap = true
					//#endif
			),
			remap = false
	)
	private void printAdditionVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
		Messenger.tell(
				source,
				Messenger.c(
						String.format("g %s ", MOD_NAME), 
					        //#if MC >= 11901
					        //$$ String.format("g %s: ", Translations.tr("carpet.settings.command.version",  "version")),
					        //#else
						String.format("g %s: ", Translations.tr("ui.version",  "version")),
					        //#endif
						String.format("g %s", CarpetTISAdditionMod.getVersion())
				)
		);
	}
}
