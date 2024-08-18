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
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12000
//$$ import net.minecraft.server.network.ServerPlayerEntity;
//#else
import net.minecraft.entity.player.PlayerEntity;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	@Inject(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/LocalDifficulty;getGlobalDifficulty()Lnet/minecraft/world/Difficulty;",
					ordinal = 0
			)
	)
	private void resetFlag_phantomLogger(CallbackInfoReturnable<Integer> cir, @Share("once") LocalBooleanRef hasLogged)
	{
		hasLogged.set(false);
	}

	@ModifyVariable(
			method = "spawn",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/LocalDifficulty;getGlobalDifficulty()Lnet/minecraft/world/Difficulty;",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 12200
					//$$ target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;"
					//#else
					target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"
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
			@Local PlayerEntity player
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
