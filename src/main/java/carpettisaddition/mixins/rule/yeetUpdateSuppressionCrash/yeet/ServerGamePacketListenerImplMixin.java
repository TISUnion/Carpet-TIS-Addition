/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.yeet;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.ExceptionCatchLocation;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin
{
	@WrapOperation(
			//#if MC >= 1.20.2
			//$$ method = "removePlayerFromWorld",
			//#else
			method = "onDisconnect",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/players/PlayerList;remove(Lnet/minecraft/server/level/ServerPlayer;)V"
			)
	)
	private void yeetUpdateSuppressionCrash_implForPlayerRemoval(PlayerList obj, ServerPlayer player, Operation<Void> original)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				original.call(obj, player);
			}
			catch (Throwable throwable)
			{
				Optional<UpdateSuppressionException> opt = UpdateSuppressionYeeter.extractInCauses(throwable);
				if (opt.isPresent())
				{
					opt.get().getSuppressionContext().report(ExceptionCatchLocation.PLAYER_REMOVAL);
				}
				else
				{
					throw throwable;
				}
			}
		}
		else
		{
			// vanilla
			original.call(obj, player);
		}
	}
}
