/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.utils.PositionUtils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.world.level.ChunkPos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface Operator
{
	int operate(CommandContext<CommandSourceStack> ctx, List<ChunkPos> chunkPosList) throws CommandSyntaxException;

	default int operateAt(CommandContext<CommandSourceStack> ctx, ChunkPos chunkPos) throws CommandSyntaxException
	{
		if (!ctx.getSource().getLevel().hasChunk(PositionUtils.chunkPosX(chunkPos), PositionUtils.chunkPosZ(chunkPos)))
		{
			throw BlockPosArgument.ERROR_NOT_LOADED.create();
		}
		return this.operate(ctx, Collections.singletonList(chunkPos));
	}

	default int operateInSquare(CommandContext<CommandSourceStack> ctx, int radius) throws CommandSyntaxException
	{
		ChunkPos center = PositionUtils.flooredChunkPos(PositionUtils.flooredBlockPos(ctx.getSource().getPosition()));
		List<ChunkPos> chunkPosList = ChunkPos.rangeClosed(center, radius).collect(Collectors.toList());
		return this.operate(ctx, chunkPosList);
	}

	default int operateInCircle(CommandContext<CommandSourceStack> ctx, int radius) throws CommandSyntaxException
	{
		ChunkPos center = PositionUtils.flooredChunkPos(PositionUtils.flooredBlockPos(ctx.getSource().getPosition()));
		List<ChunkPos> chunkPosList = ChunkPos.rangeClosed(center, radius).
				filter(chunkPos -> {
					int dx2 = (PositionUtils.chunkPosX(chunkPos) - PositionUtils.chunkPosX(center)) * (PositionUtils.chunkPosX(chunkPos) - PositionUtils.chunkPosX(center));
					int dz2 = (PositionUtils.chunkPosZ(chunkPos) - PositionUtils.chunkPosZ(center)) * (PositionUtils.chunkPosZ(chunkPos) - PositionUtils.chunkPosZ(center));
					return dx2 + dz2 <= radius * radius;
				}).
				collect(Collectors.toList());
		return this.operate(ctx, chunkPosList);
	}

	default int operateCurrent(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
	{
		return this.operateInCircle(ctx, 0);
	}
}
