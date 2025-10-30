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

package carpettisaddition.mixins.translations;

import carpet.logging.HUDController;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11900
//$$ import net.minecraft.text.MutableText;
//$$ import net.minecraft.text.Text;
//#else
import net.minecraft.network.chat.BaseComponent;
//#endif

@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	/**
	 * This handle all TISCM translation in CM hud logger
	 */
	@ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
	private static
	//#if MC >= 11900
	//$$ Text
	//#else
	BaseComponent
	//#endif
	applyTISCarpetTranslationToHudLoggerMessage(
			//#if MC >= 11900
			//$$ Text hudMessage,
			//#else
			BaseComponent hudMessage,
			//#endif

			/* ----- parent method parameters vvv -----*/

			//#if MC >= 11600
			//$$ ServerPlayerEntity player,
			//#else
			Player player,
			//#endif

			//#if MC >= 11900
			//$$ Text hudMessage_
			//#else
			BaseComponent hudMessage_
			//#endif
	)
	{
		if (player != null)
		{
			hudMessage = TISAdditionTranslations.translate(
					//#if MC >= 11900
					//$$ (MutableText)hudMessage,
					//#else
					hudMessage,
					//#endif
					(ServerPlayer)player
			);
		}
		return hudMessage;
	}
}
