/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.manipulate.chunk;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

//#if MC >= 11800
//$$ import net.minecraft.world.chunk.Chunk;
//#else
import net.minecraft.world.chunk.WorldChunk;
//#endif

@Mixin(
		//#if MC >= 11800
		//$$ Chunk.class
		//#else
		WorldChunk.class
		//#endif
)
public interface WorldChunkAccessor
{
	//#if MC >= 11800
	//$$ @Accessor("blockEntityNbts")
	//#else
	@Accessor
	//#endif
	Map<BlockPos, CompoundTag> getPendingBlockEntityTags();
}
