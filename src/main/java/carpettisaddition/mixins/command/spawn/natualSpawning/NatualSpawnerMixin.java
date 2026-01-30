/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.spawn.natualSpawning;

import carpettisaddition.commands.spawn.natualSpawning.SpawnNatualSpawningCommand;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public abstract class NatualSpawnerMixin
{
	@Inject(method = "spawnCategoryForChunk", at = @At("HEAD"), cancellable = true)
	private static void spawnNaturalSpawningCommand_cancelSpawning(
			CallbackInfo ci,
			@Local(argsOnly = true) ServerLevel level,
			@Local(argsOnly = true) MobCategory mobCategory
	)
	{
		if (!SpawnNatualSpawningCommand.getInstance().shouldSpawn(DimensionWrapper.of(level), mobCategory))
		{
			ci.cancel();
		}
	}
}
