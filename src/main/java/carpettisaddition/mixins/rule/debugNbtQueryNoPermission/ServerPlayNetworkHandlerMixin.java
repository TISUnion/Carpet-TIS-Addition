/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.debugNbtQueryNoPermission;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@ModifyExpressionValue(
			method = {
					"onQueryBlockNbt",
					"onQueryEntityNbt",
			},
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.11
					//$$ target = "Lnet/minecraft/command/permission/PermissionPredicate;hasPermission(Lnet/minecraft/command/permission/Permission;)Z"
					//#else
					target = "Lnet/minecraft/server/network/ServerPlayerEntity;allowsPermissionLevel(I)Z"
					//#endif
			)
	)
	private boolean debugNbtQueryNoPermission_removeServersideCheck(boolean hasPermission)
	{
		if (CarpetTISAdditionSettings.debugNbtQueryNoPermission)
		{
			hasPermission = true;
		}
		return hasPermission;
	}
}
