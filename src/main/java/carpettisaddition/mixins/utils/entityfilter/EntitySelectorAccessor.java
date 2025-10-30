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

package carpettisaddition.mixins.utils.entityfilter;

import carpettisaddition.utils.entityfilter.IEntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

//#if MC >= 12100
//$$ import net.minecraft.resource.featuretoggle.FeatureSet;
//#endif

//#if MC >= 11700
//$$ import net.minecraft.util.TypeFilter;
//#else
import net.minecraft.world.entity.EntityType;
//#endif

@Mixin(EntitySelector.class)
public interface EntitySelectorAccessor extends IEntitySelector
{
	@Accessor("worldLimited")
	boolean getLocalWorldOnly();

	@Accessor("position")
	Function<Vec3, Vec3> getPositionOffset();

	@Nullable
	@Accessor("aabb")
	AABB getBox();

	@Accessor("currentEntity")
	boolean getSenderOnly();

	@Nullable
	@Accessor
	String getPlayerName();

	@Nullable
	@Accessor("entityUUID")
	UUID getUuid();

	@Nullable
	@Accessor("type")
	//#if MC >= 11700
	//$$ TypeFilter<Entity, ?> getEntityFilter();
	//#else
	EntityType<?> getType();
	//#endif

	@Invoker("getPredicate")
	Predicate<Entity> invokeGetPositionPredicate(
			Vec3 vec3d
			//#if MC >= 12100
			//$$ , @Nullable Box box, @Nullable FeatureSet enabledFeatures
			//#endif
	);
}
