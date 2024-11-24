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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PositionUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

class ThrottledOperator extends TranslationContext
{
	private final Semaphore semaphore = new Semaphore(1);
	private final OperateImpl operateImpl;

	@FunctionalInterface
	public interface OperateImpl
	{
		int call(ServerCommandSource source, List<ChunkPos> chunkPosList, Runnable doneCallback);
	}

	public ThrottledOperator(ChunkManipulator chunkManipulator, OperateImpl operateImpl)
	{
		super(chunkManipulator.getTranslator());
		this.operateImpl = operateImpl;
	}

	public int operate(ServerCommandSource source, List<ChunkPos> chunkPosList)
	{
		if (!this.semaphore.tryAcquire())
		{
			Messenger.tell(source, this.tr("common.throttled"));
			return 0;
		}
		return this.operateImpl.call(source, chunkPosList, this.semaphore::release);
	}

	public int operateAt(ServerCommandSource source, ChunkPos chunkPos) throws CommandSyntaxException
	{
		if (!source.getWorld().isChunkLoaded(chunkPos.x, chunkPos.z))
		{
			throw BlockPosArgumentType.UNLOADED_EXCEPTION.create();
		}
		return this.operate(source, Collections.singletonList(chunkPos));
	}

	public int operateInSquare(ServerCommandSource source, int radius)
	{
		ChunkPos center = new ChunkPos(PositionUtils.flooredBlockPos(source.getPosition()));
		List<ChunkPos> chunkPosList = ChunkPos.stream(center, radius).collect(Collectors.toList());
		return this.operate(source, chunkPosList);
	}

	public int operateInCircle(ServerCommandSource source, int radius)
	{
		ChunkPos center = new ChunkPos(PositionUtils.flooredBlockPos(source.getPosition()));
		List<ChunkPos> chunkPosList = ChunkPos.stream(center, radius).
				filter(chunkPos -> (chunkPos.x - center.x) * (chunkPos.x - center.x) + (chunkPos.z - center.z) * (chunkPos.z - center.z) <= radius * radius).
				collect(Collectors.toList());
		return this.operate(source, chunkPosList);
	}

	public int operateCurrent(ServerCommandSource source)
	{
		return this.operateInCircle(source, 0);
	}
}
