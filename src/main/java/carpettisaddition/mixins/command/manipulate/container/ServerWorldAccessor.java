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

package carpettisaddition.mixins.command.manipulate.container;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11700
//$$ import net.minecraft.world.level.entity.EntityTickList;
//#else
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
//#endif

@Mixin(ServerLevel.class)
public interface ServerWorldAccessor
{
	//#if MC >= 11700
	//$$ @Accessor
	//$$ EntityTickList getEntityList();
	//#endif

	//#if MC < 11700
	@Accessor
	Int2ObjectMap<Entity> getEntitiesById();

	@Accessor("tickingEntities")
	boolean isTickingEntity();
	//#endif

	@Accessor("blockEvents")
	ObjectLinkedOpenHashSet<BlockEventData> getPendingBlockActions();
}
