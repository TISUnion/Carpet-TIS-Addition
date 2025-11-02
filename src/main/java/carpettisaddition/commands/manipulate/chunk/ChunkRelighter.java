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
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChunkRelighter extends TranslationContext
{
	private final ServerLevel world;
	private final List<ChunkPos> chunkPosList;
	private final CommandSourceStack source;
	private final List<LevelChunk> chunks;

	protected ChunkRelighter(Translator translator, List<ChunkPos> chunkPosList, CommandSourceStack source)
	{
		super(translator);
		this.world = source.getLevel();
		this.chunkPosList = chunkPosList;
		this.source = source;
		this.chunks = Lists.newArrayList();
	}

	public CompletableFuture<Void> relight()
	{
		long startTime = System.currentTimeMillis();
		for (ChunkPos chunkPos : this.chunkPosList)
		{
			this.chunks.add(this.world.getChunk(chunkPos.x, chunkPos.z));
		}

		List<CompletableFuture<Void>> relightFutures = Lists.newArrayList();
		for (LevelChunk chunk : chunks)
		{
			relightFutures.add(this.relightOneChunk(chunk));
		}

		Messenger.tell(this.source, tr("relighting", chunkPosList.size()));
		return CompletableFuture.allOf(relightFutures.toArray(new CompletableFuture[0])).thenRunAsync(
				() -> {
					double costSec = (System.currentTimeMillis() - startTime) / 1000.0;
					for (ChunkPos chunkPos : this.chunkPosList)
					{
						this.refreshChunkLight(chunkPos);
					}
					Messenger.tell(this.source, tr("done", chunkPosList.size(), StringUtils.fractionDigit(costSec, 1)));
					Messenger.tell(this.source, tr("hint"));
				},
				this.source.getServer()
		);
	}

	//#if MC >= 12000
	//$$ // idk why mojang deprecates this, whatever, who care xd
	//$$ @SuppressWarnings("removal")
	//#endif
	public CompletableFuture<Void> relightOneChunk(LevelChunk chunk)
	{
		ThreadedLevelLightEngine lightingProvider = this.world.getChunkSource().getLightEngine();
		ChunkPos chunkPos = chunk.getPos();
		int startX = chunkPos.getMinBlockX();
		int endX = chunkPos.getMaxBlockX();
		int startZ = chunkPos.getMinBlockZ();
		int endZ = chunkPos.getMaxBlockZ();

		//#if MC >= 11904
		//$$ int startY = chunk.getMinBuildHeight();
		//#else
		int startY = 0;
		//#endif
		int endY = chunk.getHighestSectionPosition() + 16 - 1;

		for (int x = startX; x <= endX; x++)
		{
			for (int z = startZ; z <= endZ; z++)
			{
				for (int y = endY; y >= startY; y--)
				{
					lightingProvider.checkBlock(new BlockPos(x, y, z));
				}
			}
		}
		return ChunkManipulatorUtils.enqueueDummyLightingTask(lightingProvider, chunkPos).thenRunAsync(
				() -> refreshChunkLight(chunkPos),
				this.world.getServer()
		);
	}

	// ============================== Chunk Light Refresher ==============================

	private void refreshChunkLight(ChunkPos chunkPos)
	{
		ChunkManipulatorUtils.refreshChunkLight(this.world, chunkPos);
	}
}
