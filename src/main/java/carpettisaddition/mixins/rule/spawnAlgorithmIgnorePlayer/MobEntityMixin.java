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
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Redirect(
            method = "checkDespawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/Entity;D)Lnet/minecraft/entity/player/PlayerEntity;")

    )
    private PlayerEntity getClosestPlayer(World world, Entity entity, double distance) {
        if (CarpetTISAdditionSettings.spawnAlgorithmIgnorePlayer) {
            return GetValidPlayer.getClosestValidPlayer(
                    (ServerWorld) world,
                    //#if MC<11500
                    //$$ entity.x, entity.y, entity.z
                    //#else
                    entity.getX(), entity.getY(), entity.getZ()
                    //#endif
            );
        }
        return world.getClosestPlayer(entity, distance);
    }
}
