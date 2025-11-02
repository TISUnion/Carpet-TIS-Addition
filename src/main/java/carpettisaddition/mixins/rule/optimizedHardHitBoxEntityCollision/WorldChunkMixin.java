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

package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

/**
 * See {@link EntityTrackingSectionMixin} for impl for mc >= 1.17
 */
@Restriction(
		require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"),
		conflict = @Condition(ModIds.async)
)
@Mixin(LevelChunk.class)
public abstract class WorldChunkMixin
{
	@Shadow @Final private ClassInstanceMultiMap<Entity>[] entitySections;

	private ClassInstanceMultiMap<Entity>[] hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	@SuppressWarnings("unchecked")
	@Inject(
			//#if MC >= 11500
			method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V",
			//#else
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;[Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At("TAIL")
	)
	private void optimizedHardHitBoxEntityCollision_onConstruct(CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = (ClassInstanceMultiMap<Entity>[])(new ClassInstanceMultiMap[this.entitySections.length]);
		for(int i = 0; i < this.hardHitBoxEntitySections.length; ++i)
		{
			this.hardHitBoxEntitySections[i] = new ClassInstanceMultiMap<>(Entity.class);
		}
		this.optimizedHHBECEnabled = CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision;
	}

	@Inject(method = "addEntity", at = @At("TAIL"))
	private void optimizedHardHitBoxEntityCollision_onAddEntity(Entity entity, CallbackInfo ci, @Local(ordinal = 2) int k)
	{
		if (this.optimizedHHBECEnabled)
		{
			if (OptimizedHardHitBoxEntityCollisionHelper.hasHardHitBox(entity))
			{
				this.hardHitBoxEntitySections[k].add(entity);
			}
		}
	}

	@Inject(method = "removeEntity(Lnet/minecraft/world/entity/Entity;I)V", at = @At("TAIL"))
	private void optimizedHardHitBoxEntityCollision_onRemoveEntity(Entity entity, int i, CallbackInfo ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections[i].remove(entity);
		}
	}

	/**
	 * this method is used in {@link Level#getEntities(Entity, AABB, Predicate)}
	 * which will be invoked in {@link EntityGetter#getEntityCollisions}
	 */
	@ModifyExpressionValue(
			method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/List;Ljava/util/function/Predicate;)V",
			at = @At(
					value = "FIELD",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/level/chunk/LevelChunk;entitySections:[Lnet/minecraft/util/ClassInstanceMultiMap;"
					//#else
					target = "Lnet/minecraft/world/level/chunk/LevelChunk;entitySections:[Lnet/minecraft/util/ClassInstanceMultiMap;"
					//#endif
			),
			//#if MC >= 11600
			//$$ require = 3
			//#else
			require = 4
			//#endif
	)
	private ClassInstanceMultiMap<Entity>[] optimizedHardHitBoxEntityCollision_redirectEntitySections(ClassInstanceMultiMap<Entity>[] entitySections)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			entitySections = this.hardHitBoxEntitySections;
		}
		return entitySections;
	}
}
