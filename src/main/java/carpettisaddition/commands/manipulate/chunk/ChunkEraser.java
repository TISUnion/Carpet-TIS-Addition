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

import carpettisaddition.mixins.command.manipulate.chunk.HeightmapAccessor;
import carpettisaddition.mixins.command.manipulate.chunk.PalettedContainerAccessor;
import carpettisaddition.mixins.command.manipulate.chunk.ServerWorldAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

//#if MC >= 11904
//$$ import net.minecraft.util.function.LazyIterationConsumer;
//#endif

//#if MC >= 11800
//$$ import carpettisaddition.mixins.command.manipulate.chunk.WorldTickSchedulerAccessor;
//$$ import net.minecraft.block.Blocks;
//$$ import net.minecraft.world.tick.ChunkTickScheduler;
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//$$ import java.util.function.Consumer;
//#endif

//#if MC >= 11700
//$$ import carpettisaddition.mixins.command.manipulate.chunk.ServerEntityManagerAccessor;
//#endif

public class ChunkEraser extends TranslationContext
{
	private final List<ChunkPos> chunkPosList;
	private final Map<ChunkPos, WorldChunk> chunks;
	private final ServerCommandSource source;
	private final ServerWorld world;
	private final Stats stats;

	protected ChunkEraser(Translator translator, List<ChunkPos> chunkPosList, ServerCommandSource source)
	{
		super(translator);
		this.chunks = Maps.newLinkedHashMap();
		this.chunkPosList = chunkPosList;
		this.source = source;
		this.world = source.getWorld();
		this.stats = new Stats();
	}

	private static class Stats
	{
		public int entity = 0;
		public int blockEntity = 0;
		public int tileTick = 0;
		public int blockEvent = 0;
		public int chunkSection = 0;
	}

	public CompletableFuture<Void> erase()
	{
		long startTime = System.currentTimeMillis();
		for (ChunkPos chunkPos : this.chunkPosList)
		{
			this.chunks.put(chunkPos, this.world.getChunk(chunkPos.x, chunkPos.z));
		}

		this.eraseMatter();
		this.reportMatterStats();
		return this.eraseLight().thenRunAsync(
				() -> this.reportFinalStats(System.currentTimeMillis() - startTime),
				this.world.getServer()
		);
	}

	private void reportMatterStats()
	{
		Messenger.tell(this.source, tr(
				"matter_summary", this.chunks.size(),
				this.stats.entity, this.stats.blockEntity, this.stats.tileTick, this.stats.blockEvent, this.stats.chunkSection
		));
		if (this.chunks.size() > 50)
		{
			Messenger.tell(this.source, tr("erase_light"));
		}
	}

	private void reportFinalStats(long totalCostMs)
	{
		double costSec = totalCostMs / 1000.0;
		Messenger.tell(this.source, tr("all_done", StringUtil.fractionDigit(costSec, 1)));
	}

	// ============================== Matter Eraser ==============================

	private void eraseMatter()
	{
		for (ChunkPos chunkPos : this.chunks.keySet())
		{
			this.eraseOneChunk(chunkPos);
		}
		this.eraseDataStructures();
	}

	//#if MC >= 11700
	//$$ @SuppressWarnings("unchecked")
	//#endif
	private void eraseOneChunk(ChunkPos chunkPos)
	{
		WorldChunk chunk = Objects.requireNonNull(this.chunks.get(chunkPos));

		// entity
		//#if MC >= 11700
		//$$ // Directly access the tracking sections, to make sure we can access all entities in any tracking status
		//$$ List<Entity> entities = Lists.newArrayList();
		//$$ ((ServerEntityManagerAccessor<Entity>)((ServerWorldAccessor)this.world).getEntityManager()).
		//$$ 		getCache().getTrackingSections(chunkPos.toLong()).
		//$$ 		forEach(section -> section.stream().filter(entity -> !(entity instanceof PlayerEntity)).forEach(entities::add));
		//$$ entities.forEach(Entity::discard);
		//#else
		List<Entity> entities = Arrays.stream(chunk.getEntitySectionArray()).
				flatMap(Collection::stream).
				filter(entity -> !(entity instanceof PlayerEntity)).
				collect(Collectors.toList());
		entities.forEach(Entity::remove);
		//#endif
		this.stats.entity += entities.size();

		// block entity
		List<BlockPos> bePos = Lists.newArrayList(chunk.getBlockEntities().keySet());
		bePos.forEach(this.world::removeBlockEntity);
		this.stats.blockEntity += bePos.size();

		// tile ticks
		//#if MC >= 11800
		//$$ Consumer<WorldTickScheduler<?>> eraseTileTicks = scheduler -> {
		//$$ 	ChunkTickScheduler<?> chunkTickScheduler = ((WorldTickSchedulerAccessor<?>)scheduler).getChunkTickSchedulers().get(chunk.getPos().toLong());
		//$$ 	this.stats.tileTick += chunkTickScheduler != null ? chunkTickScheduler.getTickCount() : 0;
		//$$ 	scheduler.removeChunkTickScheduler(chunk.getPos());
		//$$ };
		//$$ eraseTileTicks.accept(this.world.getBlockTickScheduler());
		//$$ eraseTileTicks.accept(this.world.getFluidTickScheduler());
		//#else
		this.stats.tileTick += this.world.getBlockTickScheduler().getScheduledTicksInChunk(chunk.getPos(), true, true).size();
		this.stats.tileTick += this.world.getFluidTickScheduler().getScheduledTicksInChunk(chunk.getPos(), true, true).size();
		//#endif

		// block events
		// done in eraseDataStructures()

		// block
		for (ChunkSection chunkSection : chunk.getSectionArray())
		{
			if (chunkSection != null)
			{
				eraseChunkSectionBlocks(chunkSection);
				this.stats.chunkSection++;
			}
		}

		// heightmap
		chunk.getHeightmaps().stream().map(Map.Entry::getValue).forEach(heightmap -> {
			//#if MC >= 11700
			//$$ int bottomY = chunk.getBottomY();
			//#else
			int bottomY = 0;
			//#endif
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					// ref: net.minecraft.world.Heightmap#trackUpdate, on what vanilla does when all blocks fail the predicate
					((HeightmapAccessor)heightmap).invokeSet(x, z, bottomY);
				}
			}
		});

		// done
		//#if MC >= 12102
		//$$ chunk.markNeedsSaving();
		//#else
		chunk.setShouldSave(true);
		//#endif
		ChunkManipulatorUtils.refreshChunk(this.world, chunk);
	}

	@SuppressWarnings("unchecked")
	private static void eraseChunkSectionBlocks(ChunkSection chunkSection)
	{
		PalettedContainer<BlockState> container = chunkSection.getContainer();
		PalettedContainerAccessor<BlockState> accessor = (PalettedContainerAccessor<BlockState>)container;

		container.lock();
		//#if MC >= 11800
		//$$ PalettedContainer.Data<BlockState> data = accessor.invokeGetCompatibleData(null, 0);
		//$$ data.palette().index(Blocks.AIR.getDefaultState());
		//$$ accessor.setData(data);
		//#else
		accessor.invokeSetPaletteSize(0);
		//#endif
		container.unlock();

		chunkSection.calculateCounts();
	}

	private void eraseDataStructures()
	{
		// block events
		ObjectLinkedOpenHashSet<BlockAction> blockEventQueue = ((ServerWorldAccessor)this.world).getPendingBlockActions();
		for (ObjectListIterator<BlockAction> iterator = blockEventQueue.iterator(); iterator.hasNext(); )
		{
			BlockAction blockAction = iterator.next();
			if (this.chunks.containsKey(new ChunkPos(blockAction.getPos())))
			{
				this.stats.blockEvent++;
				iterator.remove();
			}
		}
	}

	// ============================== Light Eraser ==============================

	private CompletableFuture<Void> eraseLight()
	{
		List<CompletableFuture<Void>> futures = Lists.newArrayList();
		for (WorldChunk chunk : this.chunks.values())
		{
			futures.add(relightChunk(this.world.getChunkManager().getLightingProvider(), chunk));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}

	private static final ChunkNibbleArray FILLED_CHUNK_NIBBLE_ARRAY = Util.make(() -> {
		// see ChunkNibbleArray#ChunkNibbleArray(byte[]) for the expected byte[] length
		byte[] bytes = new byte[2048];
		Arrays.fill( bytes, (byte)0xFF);
		return new ChunkNibbleArray(bytes);
	});

	private CompletableFuture<Void> relightChunk(ServerLightingProvider lightingProvider, WorldChunk chunk)
	{
		ChunkPos chunkPos = chunk.getPos();

		// Empty ChunkNibbleArray will not be saved to the disk during chunk serialization
		// Then upon chunk load, the world will relight the chunk automatically
		// This means actually we can just do nothing and ask the user to reload the chunk for a relight
		// Notes that after relight, the skylight storage is always filled with 1 (there's nothing in the chunk),
		// so why not do that now

		// reference: net.minecraft.server.world.ServerLightingProvider#updateChunkStatus
		//#if MC >= 11700
		//$$ int minY = lightingProvider.getBottomY();
		//$$ int maxY = lightingProvider.getTopY();
		//#else
		int minY = -1;
		int maxY = 17;
		//#endif
		for (int y = minY; y < maxY; y++)
		{
			// TODO: figure out why this is costly
			ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(chunkPos, y);
			lightingProvider.queueData(
					LightType.BLOCK, chunkSectionPos, new ChunkNibbleArray()
					//#if 11600 <= MC && MC < 12000
					//$$ , true
					//#endif
			);
			lightingProvider.queueData(
					// there's no skyLightProvider in the nether, so this queueData() call will be ignored,
					// so it's fine to fill the nether skylight
					LightType.SKY, chunkSectionPos, FILLED_CHUNK_NIBBLE_ARRAY.copy()
					//#if 11600 <= MC && MC < 12000
					//$$ , true
					//#endif
			);
		}

		return ChunkManipulatorUtils.enqueueDummyLightingTask(lightingProvider, chunkPos).thenRunAsync(
				() -> ChunkManipulatorUtils.refreshChunkLight(this.world, chunk),
				this.world.getServer()
		);
	}
}
