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

package carpettisaddition.mixins.logger.wanderingTrader;

import carpettisaddition.logging.loggers.wanderingTrader.WanderingTraderLogger;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderManagerMixin
{
	@Inject(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/npc/WanderingTrader;setDespawnDelay(I)V"
			)
	)
	private void wanderingTraderLogger_logSpawns(
			CallbackInfoReturnable<Boolean> cir,
			@Local Player playerEntity,
			@Local WanderingTrader wanderingTraderEntity
	)
	{
		WanderingTraderLogger.getInstance().onWanderingTraderSpawnSuccess(playerEntity, wanderingTraderEntity);
	}

	@ModifyExpressionValue(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;getRandomPlayer()Lnet/minecraft/server/level/ServerPlayer;"
			)
	)
	private ServerPlayer wanderingTraderLogger_recordPlayer(ServerPlayer player, @Share("spawnerPlayer") LocalRef<ServerPlayer> spawnerPlayer)
	{
		spawnerPlayer.set(player);
		return player;
	}

	@ModifyExpressionValue(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.16
					//$$ target = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;findSpawnPositionNear(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/core/BlockPos;"
					//#else
					target = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;findSpawnPositionNear(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/core/BlockPos;"
					//#endif
			)
	)
	private BlockPos wanderingTraderLogger_recordPos(BlockPos pos, @Share("spawnPos") LocalRef<BlockPos> spawnPos)
	{
		spawnPos.set(pos);
		return pos;
	}

	@Inject(
			method = "spawn",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 1.16
							//$$ target = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;findSpawnPositionNear(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/core/BlockPos;"
							//#else
							target = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;findSpawnPositionNear(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/core/BlockPos;"
							//#endif
					)
			),
			at = @At("RETURN")
	)
	private void wanderingTraderLogger_logFails(
			CallbackInfoReturnable<Boolean> cir,
			@Share("spawnerPlayer") LocalRef<ServerPlayer> spawnerPlayer,
			@Share("spawnPos") LocalRef<BlockPos> spawnPos
	)
	{
		if (!cir.getReturnValue() && spawnerPlayer.get() != null)
		{
			WanderingTraderLogger.getInstance().onWanderingTraderSpawnFail(spawnerPlayer.get(), spawnPos.get());
		}
	}
}
