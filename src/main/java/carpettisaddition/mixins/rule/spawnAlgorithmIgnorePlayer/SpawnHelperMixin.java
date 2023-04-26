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

package carpettisaddition.mixins.rule.spawnAlgorithmIgnorePlayer;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.spawnAlgorithmIgnorePlayer.GetValidPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @Redirect(
            //#if MC < 11500
            //$$ method = "spawnEntitiesInChunk",
            //#elseif MC >= 11600
            //$$ method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            //#else
            method = "spawnEntitiesInChunk(Lnet/minecraft/entity/EntityCategory;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/util/math/BlockPos;)V",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 11600
                    //$$ target = "Lnet/minecraft/server/world/ServerWorld;getClosestPlayer(DDDDZ)Lnet/minecraft/entity/player/PlayerEntity;"
                    //#else
                    target = "Lnet/minecraft/server/world/ServerWorld;getClosestPlayer(DDD)Lnet/minecraft/entity/player/PlayerEntity;"
                    //#endif
            )
    )
    private static PlayerEntity getClosestPlayer(
            //#if MC >= 11600
            //$$ ServerWorld world, double x, double y, double z, double maxDistance, boolean ignoreCreative
            //#else
            ServerWorld world, double x, double z, double maxDistance
            //#endif
    ) {
        if (CarpetTISAdditionSettings.spawnAlgorithmIgnorePlayer) {
            //#if MC >= 11600
            //$$ return GetValidPlayer.getClosestValidPlayer(world, x, y, z);
            //#else
            // Yes, MC <= 1.15 Minecraft finds horizontally closest player instead of closest player in a straight line while spawn a mob
            // 是的，你没看错，1.15及以下版本，Minecraft在刷怪时，选择的是水平距离最近的玩家，而非直线距离最近的玩家
            return GetValidPlayer.getHorizontallyClosestValidPlayer(world, x, z);
            //#endif
        }
        //#if MC >= 11600
        //$$ return world.getClosestPlayer(x, y, z, maxDistance, ignoreCreative);
        //#else
        return world.getClosestPlayer(x, z, -1);
        //#endif
    }
}
