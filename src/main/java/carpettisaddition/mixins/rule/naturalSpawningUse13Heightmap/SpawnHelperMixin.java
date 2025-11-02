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

package carpettisaddition.mixins.rule.naturalSpawningUse13Heightmap;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.naturalSpawningUse13Heightmap.NaturalSpawningUse13HeightmapHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin
{
	@ModifyExpressionValue(
			method = "getRandomPosWithin",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/chunk/LevelChunk;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I"
			)
	)
	private static int naturalSpawningUse13Heightmap_useTheHighestNonOpaqueBlock(
			int y,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) LevelChunk chunk,
			@Local(ordinal = 0) int x,
			@Local(ordinal = 1) int z
	)
	{
		if (CarpetTISAdditionSettings.naturalSpawningUse13Heightmap)
		{
			return NaturalSpawningUse13HeightmapHelper.sampleHeightmap(world, chunk, x, z, y);
		}
		return y;
	}
}
