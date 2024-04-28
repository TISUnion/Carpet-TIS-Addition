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

package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionMod;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	private static final BlockBox innerBox = new BlockBox(-78, 68, 50, -60, 77, 68);
	private static final BlockBox outerBox = new BlockBox(-83, 68, 45, -55, 77, 73);

	@Inject(
			method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;minGroupSize:I",
					ordinal = 0
			)
	)
	private static void record1(
			CallbackInfo ci,
			@Local(argsOnly = true) ServerWorld world,
			@Local BlockPos.Mutable mutable,
			@Local SpawnSettings.SpawnEntry se,
			@Share("pickEntryPos") LocalRef<BlockPos> pickEntryPos
	)
	{
		pickEntryPos.set(mutable.toImmutable());
		if (se.type == EntityType.WITHER_SKELETON && outerBox.contains(mutable) && !innerBox.contains(mutable))
		{
			if (mutable.getY() == 69 || mutable.getY() == 73 || mutable.getY() == 77)
			{
				CarpetTISAdditionMod.LOGGER.info("gt {} pick wiske at outer", world.getTime());
			}
		}
	}

	@ModifyArg(
			method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"
			)
	)
	private static Entity ww(
			Entity entity,
			@Local(argsOnly = true) ServerWorld world,
			@Local(argsOnly = true) BlockPos startPos,
			@Local BlockPos.Mutable nowPos,
			@Share("pickEntryPos") LocalRef<BlockPos> pickEntryPosRef
	)
	{
		BlockPos pickEntryPos = pickEntryPosRef.get();
		if (pickEntryPos != null && entity instanceof WitherSkeletonEntity)
		{
			Function<BlockPos, String> f = pos -> {
				return String.format("(%d,%d,%d)[i=%d,o=%d]", pos.getX(), pos.getY(), pos.getZ(), innerBox.contains(pos) ? 1 : 0, outerBox.contains(pos) ? 1 : 0);
			};
			CarpetTISAdditionMod.LOGGER.info("gt {} spawn wiske at {}, start {}, pick {}", world.getTime(), f.apply(nowPos), f.apply(startPos), f.apply(pickEntryPos));

			if (outerBox.contains(pickEntryPos) && !innerBox.contains(pickEntryPos))
			{
				CarpetTISAdditionMod.LOGGER.info("gt {} rare", world.getTime());
			}
		}
		return entity;
	}
}
