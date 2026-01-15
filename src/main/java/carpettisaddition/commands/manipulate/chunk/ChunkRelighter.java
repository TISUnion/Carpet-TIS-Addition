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

import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PositionUtils;
import carpettisaddition.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ChunkRelighter extends AbstractChunkOperator
{
	private final List<LevelChunk> chunks;

	protected ChunkRelighter(Translator translator, List<ChunkPos> chunkPosList, CommandSourceStack source)
	{
		super(translator, chunkPosList, source);
		this.chunks = Lists.newArrayList();
	}

	public CompletableFuture<Void> relight()
	{
		long startTime = System.currentTimeMillis();
		for (ChunkPos chunkPos : this.chunkPosList)
		{
			this.chunks.add(this.world.getChunk(PositionUtils.chunkPosX(chunkPos), PositionUtils.chunkPosZ(chunkPos)));
		}

		AtomicLong lastReportMs = new AtomicLong(System.currentTimeMillis());
		Set<ChunkPos> doneChunkPosSet = Sets.newConcurrentHashSet();
		AsyncBatchProcessor<LevelChunk> processor = new AsyncBatchProcessor<>(
				this.chunks,
				chunk -> this.relightOneChunk(chunk).thenApply(done -> {
					if (!done)
					{
						return null;
					}
					doneChunkPosSet.add(chunk.getPos());
					long nowMs = System.currentTimeMillis();
					if (nowMs - lastReportMs.get() >= 3000)  // every 3s
					{
						lastReportMs.set(Long.MAX_VALUE);
						final int doneCnt = doneChunkPosSet.size();
						final double chunkPerSec = doneCnt / ((nowMs - startTime) / 1000.0);
						final double etaSec = (this.chunkPosList.size() - doneCnt) / chunkPerSec;
						this.source.getServer().submit(() -> {
							lastReportMs.set(System.currentTimeMillis());
							String abortCmd = "/manipulate chunk relight abort";
							Messenger.tell(this.source, Messenger.fancy(
									tr(
											"relight_progress",
											doneCnt, this.chunkPosList.size(),
											StringUtils.fractionDigit(chunkPerSec, 1),
											StringUtils.fractionDigit(etaSec, 1)
									),
									tr("relight_progress_hover", Messenger.s(abortCmd, ChatFormatting.GRAY)),
									Messenger.ClickEvents.suggestCommand(abortCmd)
							));
						});
					}
					return null;
				}),
				5
		);

		Messenger.tell(this.source, tr("relight_start", chunkPosList.size()));
		return processor.start().thenRunAsync(
				() -> {
					int doneCnt = doneChunkPosSet.size();
					for (ChunkPos chunkPos : this.chunkPosList)
					{
						if (doneChunkPosSet.contains(chunkPos))
						{
							this.refreshChunkLight(chunkPos);
						}
					}
					String costSec = StringUtils.fractionDigit((System.currentTimeMillis() - startTime) / 1000.0, 1);
					if (doneCnt != this.chunkPosList.size())
					{
						Messenger.tell(this.source, Messenger.formatting(tr("done_aborted", doneCnt, this.chunkPosList.size(), costSec), ChatFormatting.GOLD));
					}
					else
					{
						Messenger.tell(this.source,  Messenger.formatting(tr("done", doneCnt, costSec), ChatFormatting.GREEN));
					}
					Messenger.tell(this.source, tr("hint"));
				},
				this.source.getServer()
		);
	}

	@Override
	public void abort(CommandSourceStack source)
	{
		Messenger.tell(source, this.tr("aborting"));
		super.abort(source);
	}

	//#if MC >= 12000
	//$$ // idk why mojang deprecates this, whatever, who care xd
	//$$ @SuppressWarnings("removal")
	//#endif
	private CompletableFuture<Boolean> relightOneChunk(LevelChunk chunk)
	{
		if (this.aborted.get())
		{
			return CompletableFuture.completedFuture(false);
		}

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
		).thenApply(v -> true);
	}

	// ============================== Chunk Light Refresher ==============================

	private void refreshChunkLight(ChunkPos chunkPos)
	{
		ChunkManipulatorUtils.refreshChunkLight(this.world, chunkPos);
	}
}
