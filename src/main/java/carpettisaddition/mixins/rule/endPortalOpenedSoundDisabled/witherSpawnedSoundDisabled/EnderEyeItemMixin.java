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

package carpettisaddition.mixins.rule.endPortalOpenedSoundDisabled.witherSpawnedSoundDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin
{
	@WrapWithCondition(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/World;syncGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#else
					target = "Lnet/minecraft/world/World;playGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#endif
			)
	)
	private boolean endPortalOpenedSoundDisabled_conditionCheck(World instance, int type, BlockPos blockPos, int data)
	{
		return !CarpetTISAdditionSettings.endPortalOpenedSoundDisabled;
	}
}
