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

import carpet.utils.SpawnReporter;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.natualSpawningUse13Heightmap.NatualSpawningUse13HeightmapHelper;
import carpettisaddition.utils.Messenger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

//#if MC < 11500
//$$ import net.minecraft.world.World;
//#endif

@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	@Inject(
			method = "report",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=Spawns:"
			)
	)
	private static void natualSpawningUse13Heightmap_reportTheNewHeightmapAsWell(
			CallbackInfoReturnable<List<Text>> cir,
			@Local(argsOnly = true)
			//#if MC >= 11500
			ServerWorld world,
			//#else
			//$$ World world,
			//#endif
			@Local List<BaseText> rep,
			@Local(ordinal = 0) int x,
			@Local(ordinal = 1) int y,
			@Local(ordinal = 2) int z,
			@Local Chunk chunk
	)
	{
		if (CarpetTISAdditionSettings.natualSpawningUse13Heightmap)
		{
			int lc = NatualSpawningUse13HeightmapHelper.sampleHeightmap(world, chunk, x, z) + 1;
			String relativeHeight = (y == lc) ? "right at it." : String.format("%d blocks %s it.", MathHelper.abs(y - lc), (y >= lc) ? "above" : "below");
			rep.add(Messenger.s(String.format("Maximum spawn Y value for (%+d, %+d) is %d. You are %s (13 ver)", x, z, lc, relativeHeight)));
		}
	}
}
