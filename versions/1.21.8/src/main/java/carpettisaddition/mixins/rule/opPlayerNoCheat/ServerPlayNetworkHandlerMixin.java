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

package carpettisaddition.mixins.rule.opPlayerNoCheat;

import carpettisaddition.helpers.rule.opPlayerNoCheat.OpPlayerNoCheatHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@WrapWithCondition(
			method = "onChangeGameMode",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/command/GameModeCommand;execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/GameMode;)V"
			)
	)
	private boolean checkIfAllowCheating_gameModeSwitcherScreenPacket(ServerPlayerEntity serverPlayerEntity, GameMode gameMode)
	{
		return OpPlayerNoCheatHelper.canCheat(serverPlayerEntity);
	}
}
