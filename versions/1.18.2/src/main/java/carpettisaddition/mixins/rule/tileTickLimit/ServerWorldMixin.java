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

package carpettisaddition.mixins.rule.tileTickLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@ModifyExpressionValue(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=tickPending"
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=raid"
					)
			),
			at = @At(
					value = "CONSTANT",
					args = "intValue=65536"
			),
			require = 2
	)
	private int modifyTileTickLimit(int value)
	{
		return CarpetTISAdditionSettings.tileTickLimit;
	}
}