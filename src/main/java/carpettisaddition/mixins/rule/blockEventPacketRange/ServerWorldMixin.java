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

package carpettisaddition.mixins.rule.blockEventPacketRange;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Inject with lower priority, so it's invoked after g4mespeed's @ModifyArg
@Mixin(value = ServerLevel.class, priority = 2000)
public abstract class ServerWorldMixin
{
	@ModifyArg(
			method = "runBlockEvents",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/server/PlayerManager;sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/network/Packet;)V"
					//#else
					target = "Lnet/minecraft/server/players/PlayerList;broadcast(Lnet/minecraft/world/entity/player/Player;DDDDLnet/minecraft/world/level/dimension/DimensionType;Lnet/minecraft/network/protocol/Packet;)V"
					//#endif
			),
			index = 4
	)
	private double modifyBlockEventPacketRange(double range)
	{
		// Modify only when the value of the rule has changed
		if (CarpetTISAdditionSettings.blockEventPacketRange != CarpetTISAdditionSettings.VANILLA_BLOCK_EVENT_PACKET_RANGE)
		{
			range = CarpetTISAdditionSettings.blockEventPacketRange;
		}
		return range;
	}
}
