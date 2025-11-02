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

package carpettisaddition.mixins.rule.witherSpawnedSoundDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12102
//$$ import net.minecraft.server.level.ServerLevel;
//#endif

@Mixin(WitherBoss.class)
public abstract class WitherEntityMixin
{
	@WrapWithCondition(
			method = "customServerAiStep",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;syncGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V"
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/world/level/Level;globalLevelEvent(ILnet/minecraft/core/BlockPos;I)V"
					//#else
					target = "Lnet/minecraft/world/level/Level;globalLevelEvent(ILnet/minecraft/core/BlockPos;I)V"
					//#endif
			)
	)
	private boolean witherSpawnedSoundDisabled_conditionCheck(
			//#if MC >= 12102
			//$$ ServerLevel instance,
			//#else
			Level instance,
			//#endif
			int type, BlockPos blockPos, int data
	)
	{
		return !CarpetTISAdditionSettings.witherSpawnedSoundDisabled;
	}
}
