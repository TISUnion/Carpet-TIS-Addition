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

package carpettisaddition.mixins.helpers.mixin;

import carpettisaddition.helpers.mixin.IWorldOverrides;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

//#if MC >= 11800
//$$ import org.jetbrains.annotations.Nullable;
//$$ import java.util.List;
//#else
import java.util.stream.Stream;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.world.level.CommonLevelAccessor;
//#else
import net.minecraft.world.level.LevelAccessor;
//#endif

//#if MC < 11600
import java.util.Set;
//#endif

//#if MC >= 11600 && MC < 11800
//$$ import java.util.function.Predicate;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ value = CommonLevelAccessor.class,
		//#else
		value = LevelAccessor.class,
		//#endif
		priority = 2000
)
public interface IWorldMixin extends EntityGetter
{
	/**
	 * @reason Interface injection is not supported by Mixin yet
	 * So here comes the @Overwrite
	 *
	 * @author Fallen_Breath
	 */
	@Overwrite
	//#if MC >= 11800
	//$$ default List<VoxelShape> getEntityCollisions(@Nullable Entity entity, AABB box)
	//#elseif MC >= 11600
	//$$ default Stream<VoxelShape> getEntityCollisions(Entity entity, AABB box, Predicate<Entity> predicate)
	//#elseif MC >= 11500
	default Stream<VoxelShape> getEntityCollisions(Entity entity, AABB box, Set<Entity> excluded)
	//#else
	//$$ default Stream<VoxelShape> method_20743(Entity entity, AABB box, Set<Entity> excluded)
	//#endif
	{
		IWorldOverrides.getEntityCollisionsPre(entity, box);
		try
		{
			return IWorldOverrides.getEntityCollisionsModifyResult(
					entity, box,
					// vanilla copy
					EntityGetter.super.
							//#if MC >= 11800
							//$$ getEntityCollisions(entity, box)
							//#elseif MC >= 11600
							//$$ getEntityCollisions(entity, box, predicate)
							//#elseif MC >= 11500
							getEntityCollisions(entity, box, excluded)
							//#else
							//$$ method_20743(entity, box, excluded)
							//#endif
			);
		}
		finally
		{
			IWorldOverrides.getEntityCollisionsPost(entity, box);
		}
	}
}
