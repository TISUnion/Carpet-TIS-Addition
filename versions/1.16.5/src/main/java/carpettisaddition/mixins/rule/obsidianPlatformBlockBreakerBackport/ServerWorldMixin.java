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

package carpettisaddition.mixins.rule.obsidianPlatformBlockBreakerBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.obsidianPlatformBlockBreakerBackport.ObsidianPlatformBlockBreakerBackportHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// impl in mc [1.16, 1.21)
@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	@ModifyArg(
			// lambda methods in createEndPlatform
			method = {
					"method_29201",
					"method_29204",
			},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
			)
	)
	private static BlockPos obsidianPlatformBlockBreakerBackport_tweakForNormalEntity(BlockPos pos, BlockState state, @Local(argsOnly = true) ServerLevel world)
	{
		if (CarpetTISAdditionSettings.obsidianPlatformBlockBreakerBackport)
		{
			ObsidianPlatformBlockBreakerBackportHelper.onObsidianPlatformSetBlockPre(world, pos, state);
		}
		return pos;
	}
}
