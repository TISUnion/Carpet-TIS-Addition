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
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC < 11600
import org.spongepowered.asm.mixin.injection.Slice;
//#endif

// impl in mc [~, 1.21)
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@ModifyArg(
			//#if MC >= 11600
			//$$ method = "createEndSpawnPlatform",
			//#else
			method = "changeDimension",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=placing"
					)
			),
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
					ordinal = 0
			)
	)
	private BlockPos obsidianPlatformBlockBreakerBackport_tweakForPlayerEntity(
			BlockPos pos, BlockState state,
			@Local(
					//#if MC >= 11600
					//$$ argsOnly = true
					//#else
					ordinal = 1
					//#endif
			)
			ServerWorld world
	)
	{
		if (CarpetTISAdditionSettings.obsidianPlatformBlockBreakerBackport)
		{
			ObsidianPlatformBlockBreakerBackportHelper.onObsidianPlatformSetBlockPre(world, pos, state);
		}
		return pos;
	}
}
