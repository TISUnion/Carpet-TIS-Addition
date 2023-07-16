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

package carpettisaddition.mixins.rule.minecartFullDropBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(value = {
		ChestMinecartEntity.class,
		FurnaceMinecartEntity.class,
		HopperMinecartEntity.class,
		TntMinecartEntity.class,
})
public abstract class MinecartVariablesMixin
{
	@ModifyExpressionValue(
			method = "dropItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"
			)
	)
	private boolean minecartFullDropBackport_cancelSeparatedDrop(boolean canDrop)
	{
		if (CarpetTISAdditionSettings.minecartFullDropBackport)
		{
			canDrop = false;
		}
		return canDrop;
	}
}
