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
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;
import java.util.function.Predicate;

//#if MC >= 11800
//$$ import net.minecraft.world.level.entity.EntityAccess;
//#endif

/**
 * See {@link WorldChunkMixin} for impl for mc < 1.17
 */
@Restriction(
		require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"),
		conflict = @Condition(ModIds.async)
)
@Mixin(EntitySection.class)
public abstract class EntityTrackingSectionMixin<
		T
		//#if MC >= 11800
		//$$ extends EntityAccess
		//#endif
>
{
	private ClassInstanceMultiMap<T> hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	/**
	 * Enable only if {@param entityClass} is Entity.class
	 * See the return type of ServerLevel#getEntityLookup() for the reason of using Entity.class as judgement
	 */
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void onConstruct(Class<T> entityClass, Visibility status, CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = new ClassInstanceMultiMap<>(entityClass);
		this.optimizedHHBECEnabled = CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision && entityClass == Entity.class;
	}

	@Inject(method = "add", at = @At("TAIL"))
	private void onAddEntity(T entity, CallbackInfo ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			if (entity instanceof Entity && OptimizedHardHitBoxEntityCollisionHelper.hasHardHitBox((Entity)entity))
			{
				this.hardHitBoxEntitySections.add(entity);
			}
		}
	}

	@Inject(method = "remove", at = @At("TAIL"))
	private void onRemoveEntity(T entity, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.optimizedHHBECEnabled)
		{
			this.hardHitBoxEntitySections.remove(entity);
		}
	}

	/**
	 * Invoke path:
	 * - {@link EntityGetter#getEntityCollisions}
	 * - {@link Level#getEntities(Entity, AABB, Predicate)}
	 * - {@link LevelEntityGetterAdapter#get(net.minecraft.world.phys.AABB, java.util.function.Consumer)}
	 * - {@link EntitySectionStorage#getEntities(net.minecraft.world.phys.AABB, java.util.function.Consumer)}
	 * -
	 *   (<=1.17)         {@link EntitySection#getEntities(java.util.function.Predicate, java.util.function.Consumer)}
	 *   (>=1.18 <1.19.3) {@link EntitySection#getEntities(net.minecraft.world.phys.AABB, java.util.function.Consumer)}
	 *   (>=1.19.3)       {@link EntitySection#getEntities(AABB, net.minecraft.util.AbortableIterationConsumer)}
	 *
	 * For 1.17: looks like this is the method to collect objects in this chunk section based storage
	 */
	@ModifyExpressionValue(
			//#if MC >= 1.19.3
			//$$ method = "getEntities(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/util/AbortableIterationConsumer;)Lnet/minecraft/util/AbortableIterationConsumer$Continuation;",
			//#elseif MC >= 1.18.2
			//$$ method = "getEntities(Lnet/minecraft/world/phys/AABB;Ljava/util/function/Consumer;)V",
			//#else
			method = "getEntities(Ljava/util/function/Predicate;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/level/entity/EntitySection;storage:Lnet/minecraft/util/ClassInstanceMultiMap;"
			),
			require = 1
	)
	private ClassInstanceMultiMap<T> redirectEntitySections(ClassInstanceMultiMap<T> collection)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			collection = this.hardHitBoxEntitySections;
		}
		return collection;
	}
}