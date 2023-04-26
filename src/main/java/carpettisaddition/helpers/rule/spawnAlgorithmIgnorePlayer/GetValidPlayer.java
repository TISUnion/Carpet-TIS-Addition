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

package carpettisaddition.helpers.rule.spawnAlgorithmIgnorePlayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GetValidPlayer {
    public static boolean isValidPlayerInRange(World world, double x, double y, double z, double range) {
        for (PlayerEntity playerEntity : world.getPlayers()) {
            if (!EntityPredicates.EXCEPT_SPECTATOR.test(playerEntity) || !EntityPredicates.VALID_ENTITY_LIVING.test(playerEntity)) {
                continue;
            }

            if (shouldIgnore(playerEntity)) {
                continue;
            }

            double d = playerEntity.squaredDistanceTo(x, y, z);
            if (!(range < 0.0) && !(d < range * range)) continue;
            return true;
        }
        return false;
    }

    public static PlayerEntity getHorizontallyClosestValidPlayer(ServerWorld world, double x, double z) {
        PlayerEntity closestValidPlayer = null;
        double closestPlayerDistance = Double.MAX_VALUE;
        for (PlayerEntity player : world.getPlayers()) {
            if (!EntityPredicates.EXCEPT_SPECTATOR.test(player)) {
                continue;
            }
            if (shouldIgnore(player)) {
                continue;
            }
            if (closestValidPlayer == null) {
                closestValidPlayer = player;
                closestPlayerDistance = player.squaredDistanceTo(x,
                        //#if MC<11500
                        //$$ player.y,
                        //#else
                        player.getY(),
                        //#endif
                        z);
                continue;
            }
            double currentDistance = player.squaredDistanceTo(x,
                    //#if MC<11500
                    //$$ player.y,
                    //#else
                    player.getY(),
                    //#endif
                    z);
            if (currentDistance < closestPlayerDistance) {
                closestValidPlayer = player;
                closestPlayerDistance = currentDistance;
            }
        }
        return closestValidPlayer;
    }

    public static PlayerEntity getClosestValidPlayer(ServerWorld world, double x, double y, double z) {
        PlayerEntity closestValidPlayer = null;
        double closestPlayerDistance = Double.MAX_VALUE;
        for (PlayerEntity player : world.getPlayers()) {
            if (player.isSpectator()) {
                continue;
            }
            if (shouldIgnore(player)) {
                continue;
            }
            if (closestValidPlayer == null) {
                closestValidPlayer = player;
                closestPlayerDistance = player.squaredDistanceTo(x, y, z);
                continue;
            }
            double currentDistance = player.squaredDistanceTo(x, y, z);
            if (currentDistance < closestPlayerDistance) {
                closestValidPlayer = player;
                closestPlayerDistance = currentDistance;
            }
        }
        return closestValidPlayer;
    }

    public static ServerPlayerEntity getRandomValidAlivePlayer(net.minecraft.server.world.ServerWorld serverWorld) {
        List<ServerPlayerEntity> list = serverWorld.getPlayers(LivingEntity::isAlive);
        list.removeIf(GetValidPlayer::shouldIgnore);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(serverWorld.random.nextInt(list.size()));
    }

    public static List<ServerPlayerEntity> getValidPlayers(ServerWorld world) {
        List<ServerPlayerEntity> r = new ArrayList<>();
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!shouldIgnore(player)) {
                r.add(player);
            }
        }
        return r;
    }

    private static boolean shouldIgnore(PlayerEntity playerEntity) {
        Team ignoreTeam = playerEntity.getServer().getScoreboard().getTeam("spawnAlgIgnore");

        if (ignoreTeam != null) {
            return playerEntity.isTeamPlayer(ignoreTeam);
        } else {
            playerEntity.getServer().getScoreboard().addTeam("spawnAlgIgnore");
        }
        return false;
    }
}
