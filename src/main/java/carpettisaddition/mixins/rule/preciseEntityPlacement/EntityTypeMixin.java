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

package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

//#if MC >= 11600
//$$ import net.minecraft.server.world.ServerWorld;
//#endif

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity>
{
	@ModifyVariable(
			//#if MC >= 11903
			//$$ method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
			//#elseif MC >= 11700
			//$$ method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
			//#elseif MC >= 11600
			//$$ method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
			//#else
			method = "create(Lnet/minecraft/world/World;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnType;ZZ)Lnet/minecraft/entity/Entity;",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V",
					shift = At.Shift.AFTER
			)
	)
	private T preciseEntityPlacement(
			T entity,

			//#if MC >= 11600
			//$$ ServerWorld serverWorld,
			//#else
			World world,
			//#endif

			@Nullable CompoundTag itemTag,

			//#if MC >= 11903
			//$$ @Nullable Consumer<T> afterConsumer,
			//#else
			@Nullable Text name, @Nullable PlayerEntity player,
			//#endif

			BlockPos pos, SpawnType spawnType, boolean alignPosition, boolean invertY
	)
	{
		// there's an extra spawnEggTargetPos null check in the method PreciseEntityPlacer#adjustEntityFromSpawnEgg
		// testing if the adjusting is required
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntityFromSpawnEgg(entity);
		}
		return entity;
	}
}
