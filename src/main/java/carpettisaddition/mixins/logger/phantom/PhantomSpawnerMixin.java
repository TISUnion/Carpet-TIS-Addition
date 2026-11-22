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
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//#if MC >= 1.20
//$$ import net.minecraft.server.level.ServerPlayer;
//#endif
//#if MC >= 1.21.5
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.16
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V",
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void phantomLogger_countSpawnedPhantom(
			//#if MC >= 1.21.5
			//$$ CallbackInfo ci,
			//#else
			CallbackInfoReturnable<Integer> cir,
			//#endif
			//#if MC >= 1.20
			//$$ @Local ServerPlayer player,
			//#else
			@Local Player player,
			//#endif
			@Share("spawnerPlayer") LocalRef<Player> spawnerPlayer,
			@Share("spawnedCount") LocalIntRef spawnedCount
	)
	{
		spawnerPlayer.set(player);
		spawnedCount.set(spawnedCount.get() + 1);
	}

	// The player iterator is checked again after every batch, including the last one.
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Iterator;hasNext()Z",
					remap = false
			)
	)
	private void phantomLogger_logSpawns(
			//#if MC >= 1.21.5
			//$$ CallbackInfo ci,
			//#else
			CallbackInfoReturnable<Integer> cir,
			//#endif
			@Share("spawnerPlayer") LocalRef<Player> spawnerPlayer,
			@Share("spawnedCount") LocalIntRef spawnedCount
	)
	{
		int count = spawnedCount.get();
		if (count > 0)
		{
			PhantomLogger.getInstance().onPhantomSpawn(spawnerPlayer.get(), count);
			spawnedCount.set(0);
		}
	}
}
