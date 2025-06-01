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

package carpettisaddition.mixins.rule.explosionPacketRange;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static carpettisaddition.CarpetTISAdditionSettings.VANILLA_EXPLOSION_PACKET_RANGE;

@Mixin(value = ServerWorld.class, priority = 500)
public abstract class ServerWorldMixin
{
	@ModifyExpressionValue(
			method = "createExplosion",
			at = @At(
					value = "CONSTANT",
					args = "doubleValue=4096.0" // VANILLA_EXPLOSION_PACKET_RANGE * VANILLA_EXPLOSION_PACKET_RANGE
			),
			require = 0
	)
	private double modifyExplosionPacketRange(double value)
	{
		double ruleValue = CarpetTISAdditionSettings.explosionPacketRange;
		return ruleValue != VANILLA_EXPLOSION_PACKET_RANGE ? ruleValue * ruleValue : value;
	}
}
