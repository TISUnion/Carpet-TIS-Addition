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

package carpettisaddition.mixins.rule.natualSpawningUse13Heightmap;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.natualSpawningUse13Heightmap.NatualSpawningUse13HeightmapHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin
{
	@ModifyExpressionValue(
			//#if MC >= 11500
			method = "getSpawnPos",
			//#else
			//$$ method = "method_8657",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/chunk/WorldChunk;sampleHeightmap(Lnet/minecraft/world/Heightmap$Type;II)I"
			)
	)
	private static int natualSpawningUse13Heightmap_useTheHighestNonOpaqueBlock(
			int y,
			@Local(argsOnly = true) World world,
			@Local(argsOnly = true) WorldChunk chunk,
			@Local(ordinal = 0) int x,
			@Local(ordinal = 1) int z
	)
	{
		if (CarpetTISAdditionSettings.natualSpawningUse13Heightmap)
		{
			return NatualSpawningUse13HeightmapHelper.sampleHeightmap(world, chunk, x, z, y);
		}
		return y;
	}
}
