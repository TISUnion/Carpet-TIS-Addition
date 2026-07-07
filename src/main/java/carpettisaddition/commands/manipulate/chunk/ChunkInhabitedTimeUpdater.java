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

import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PositionUtils;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;

public class ChunkInhabitedTimeUpdater extends AbstractChunkOperator
{
	protected ChunkInhabitedTimeUpdater(Translator translator, List<ChunkPos> chunkPosList, CommandSourceStack source)
	{
		super(translator, chunkPosList, source);
	}

	private List<LevelChunk> getChunks()
	{
		List<LevelChunk> chunks = Lists.newArrayList();
		for (ChunkPos chunkPos : this.chunkPosList)
		{
			LevelChunk chunk = this.world.getChunk(PositionUtils.chunkPosX(chunkPos), PositionUtils.chunkPosZ(chunkPos));
			chunks.add(chunk);
		}
		return chunks;
	}

	public int set(long ticks)
	{
		List<LevelChunk> chunks = getChunks();
		for (LevelChunk chunk : chunks)
		{
			chunk.setInhabitedTime(ticks);
		}
		Messenger.tell(this.source, tr(
				"set_done",
				Messenger.s(chunks.size(), ChatFormatting.GOLD),
				Messenger.s(ticks, ChatFormatting.YELLOW))
		);
		return chunks.size();
	}

	public int add(long delta)
	{
		List<LevelChunk> chunks = getChunks();
		for (LevelChunk chunk : chunks)
		{
			chunk.setInhabitedTime(Math.max(0L, chunk.getInhabitedTime() + delta));
		}
		Messenger.tell(this.source, tr(
				"add_done",
				Messenger.s(chunks.size(), ChatFormatting.GOLD),
				Messenger.s(delta, ChatFormatting.YELLOW)
		));

		if (!chunks.isEmpty())
		{
			Stats stats = this.computeStats(chunks);
			Messenger.tell(this.source, tr(
					"add_summary",
					Messenger.s(chunks.size(), ChatFormatting.GOLD),
					Messenger.s(stats.minTime, ChatFormatting.DARK_AQUA),
					Messenger.s(stats.maxTime , ChatFormatting.AQUA),
					Messenger.s((long)stats.avgTime , ChatFormatting.DARK_PURPLE)
			));
		}
		return chunks.size();
	}

	public int query()
	{
		List<LevelChunk> chunks = getChunks();
		if (chunks.isEmpty())
		{
			Messenger.tell(this.source, tr("query_no_chunk"));
		}
		else
		{
			Stats stats = this.computeStats(chunks);
			Messenger.tell(this.source, tr(
					"query_summary",
					Messenger.s(chunks.size(), ChatFormatting.GOLD),
					Messenger.s(stats.minTime, ChatFormatting.DARK_AQUA),
					Messenger.s(stats.maxTime , ChatFormatting.AQUA),
					Messenger.s((long)stats.avgTime , ChatFormatting.DARK_PURPLE)
			));
		}
		return chunks.size();
	}

	private static class Stats
	{
		long minTime;
		long maxTime;
		double avgTime;
	}

	private Stats computeStats(List<LevelChunk> chunks)
	{
		Stats stats = new Stats();
		stats.minTime = Long.MAX_VALUE;
		stats.maxTime = Long.MIN_VALUE;
		double totalTime = 0;
		for (LevelChunk chunk : chunks)
		{
			long time = chunk.getInhabitedTime();
			stats.minTime = Math.min(stats.minTime, time);
			stats.maxTime = Math.max(stats.maxTime, time);
			totalTime += time;
		}
		stats.avgTime = chunks.isEmpty() ? 0 : totalTime / chunks.size();
		return stats;
	}
}
