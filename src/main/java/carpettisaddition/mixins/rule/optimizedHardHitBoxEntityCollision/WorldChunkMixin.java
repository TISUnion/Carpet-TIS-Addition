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
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
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
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin
{
	@Shadow @Final private TypeFilterableList<Entity>[] entitySections;

	private TypeFilterableList<Entity>[] hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	@SuppressWarnings("unchecked")
	@Inject(
			//#if MC >= 11500
			method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V",
			//#else
			//$$ method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;[Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At("TAIL")
	)
	private void optimizedHardHitBoxEntityCollision_onConstruct(CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = (TypeFilterableList<Entity>[])(new TypeFilterableList[this.entitySections.length]);
		for(int i = 0; i < this.hardHitBoxEntitySections.length; ++i)
		{
			this.hardHitBoxEntitySections[i] = new TypeFilterableList<>(Entity.class);
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

	@Inject(method = "remove(Lnet/minecraft/entity/Entity;I)V", at = @At("TAIL"))
	private void optimizedHardHitBoxEntityCollision_onRemoveEntity(Entity entity, int i, CallbackInfo ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections[i].remove(entity);
		}
	}

	/**
	 * this method is used in
	 *   (>=1.16) {@link World#getOtherEntities(Entity, Box, Predicate)}
	 *   (<=1.15) {@link World#getEntities(Entity, Box, Predicate)}
	 * which will be invoked in
	 *   (>=1.15) {@link EntityView#getEntityCollisions}
	 *   (<=1.14) {@link EntityView#method_20743}
	 */
	@ModifyExpressionValue(
			//#if MC >= 11600
			//$$ method = "collectOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/List;Ljava/util/function/Predicate;)V",
			//#else
			method = "getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/List;Ljava/util/function/Predicate;)V",
			//#endif
			at = @At(
					value = "FIELD",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/chunk/WorldChunk;entitySections:[Lnet/minecraft/util/collection/TypeFilterableList;"
					//#else
					target = "Lnet/minecraft/world/chunk/WorldChunk;entitySections:[Lnet/minecraft/util/TypeFilterableList;"
					//#endif
			),
			//#if MC >= 11600
			//$$ require = 3
			//#else
			require = 4
			//#endif
	)
	private TypeFilterableList<Entity>[] optimizedHardHitBoxEntityCollision_redirectEntitySections(TypeFilterableList<Entity>[] entitySections)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			entitySections = this.hardHitBoxEntitySections;
		}
		return entitySections;
	}
}
