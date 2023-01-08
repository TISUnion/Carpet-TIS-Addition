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
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.EntityTrackingStatus;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

//#if MC >= 11800
//$$ import net.minecraft.world.entity.EntityLike;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(EntityTrackingSection.class)
public abstract class EntityTrackingSectionMixin<
		T
		//#if MC >= 11800
		//$$ extends EntityLike
		//#endif
>
{
	// just like WorldChunk#entitySections in 1.16- but it's per chunk section and it uses genericity
	@Shadow @Final private TypeFilterableList<T> collection;

	private TypeFilterableList<T> hardHitBoxEntitySections;
	private boolean optimizedHHBECEnabled;  // optimizedHardHitBoxEntityCollisionEnabled

	/**
	 * Enable only if {@param entityClass} is Entity.class
	 * See the return type of ServerWorld#getEntityLookup() for the reason of using Entity.class as judgement
	 */
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void onConstruct(Class<T> entityClass, EntityTrackingStatus status, CallbackInfo ci)
	{
		this.hardHitBoxEntitySections = new TypeFilterableList<>(entityClass);
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
	 * - {@link EntityView#getEntityCollisions}
	 * - {@link World#getOtherEntities(Entity, Box, Predicate)}
	 * - {@link SimpleEntityLookup#forEachIntersects(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 * - {@link SectionedEntityCache#forEachIntersects(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 * -
	 *   (<=1.17)         {@link EntityTrackingSection#forEach(java.util.function.Predicate, java.util.function.Consumer)}
	 *   (>=1.18 <1.19.3) {@link EntityTrackingSection#forEach(net.minecraft.util.math.Box, java.util.function.Consumer)}
	 *   (>=1.19.3)       {@link EntityTrackingSection#forEach(Box, net.minecraft.util.function.LazyIterationConsumer)}
	 *
	 * For 1.17: looks like this is the method to collect objects in this chunk section based storage
	 */
	@Redirect(
			//#if MC >= 11903
			//$$ method = "forEach(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;",
			//#elseif MC >= 11800
			//$$ method = "forEach(Lnet/minecraft/util/math/Box;Ljava/util/function/Consumer;)V",
			//#else
			method = "forEach(Ljava/util/function/Predicate;Ljava/util/function/Consumer;)V",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/EntityTrackingSection;collection:Lnet/minecraft/util/collection/TypeFilterableList;"
			),
			require = 1
	)
	private TypeFilterableList<T> redirectEntitySections(EntityTrackingSection<T> section)
	{
		if (this.optimizedHHBECEnabled && OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.get())
		{
			return this.hardHitBoxEntitySections;
		}
		// vanilla
		return this.collection;
	}
}