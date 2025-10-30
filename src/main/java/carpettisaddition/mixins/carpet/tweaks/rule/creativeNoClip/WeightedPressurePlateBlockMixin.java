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

package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * see {@link AbstractPressurePlateBlockMixin} in mc1.20+
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.20"))
@Mixin(WeightedPressurePlateBlock.class)
public abstract class WeightedPressurePlateBlockMixin
{
	@Inject(
			method = "getSignalStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
			)
	)
	private void dontDetectCreativeNoClipPlayers_weightedPressurePlate_enter(CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.exceptSpectatorPredicateIgnoreNoClipPlayers.set(true);
		}
	}

	@Inject(
			method = "getSignalStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private void dontDetectCreativeNoClipPlayers_weightedPressurePlate_exit(CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.exceptSpectatorPredicateIgnoreNoClipPlayers.set(false);
		}
	}
}
