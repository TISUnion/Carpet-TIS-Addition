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

package carpettisaddition.mixins.logger.phantom;

import carpettisaddition.logging.loggers.phantom.PhantomLogger;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

//#if MC >= 12000
//$$ import net.minecraft.server.network.ServerPlayerEntity;
//#else
import net.minecraft.world.entity.player.Player;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	@ModifyVariable(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/DifficultyInstance;getDifficulty()Lnet/minecraft/world/Difficulty;",
					ordinal = 0
			),
			argsOnly = true
	)
	private ServerLevel resetFlag_phantomLogger(ServerLevel serverWorld, @Share("once") LocalBooleanRef hasLogged)
	{
		hasLogged.set(false);
		return serverWorld;
	}

	@ModifyVariable(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/DifficultyInstance;getDifficulty()Lnet/minecraft/world/Difficulty;",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;"
					//#else
					target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"
					//#endif
			),
			ordinal = 3
	)
	private int doLog_phantomLogger(
			int amount,
			@Share("once") LocalBooleanRef hasLogged,
			//#if MC >= 12000
			//$$ @Local ServerPlayerEntity player
			//#else
			@Local Player player
			//#endif
	)
	{
		if (!hasLogged.get())
		{
			PhantomLogger.getInstance().onPhantomSpawn(player, amount);
			hasLogged.set(true);
		}
		return amount;
	}
}
