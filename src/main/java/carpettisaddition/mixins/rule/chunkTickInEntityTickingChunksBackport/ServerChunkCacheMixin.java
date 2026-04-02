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

package carpettisaddition.mixins.rule.chunkTickInEntityTickingChunksBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.18
//$$ import carpettisaddition.utils.PositionUtils;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.21.5"))
@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin
{
	@Shadow @Final
	private ServerLevel level;

	@Shadow @Final
	public ChunkMap chunkMap;

	@SuppressWarnings("RedundantIfStatement")
	@WrapWithCondition(
			//#if MC >= 1.21.3
			//$$ method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;JLjava/util/List;)V",
			//#elseif MC >= 1.18
			//$$ method = "tickChunks",
			//#else
			method = "method_20801",  // lambda method in tickChunks
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V"
			)
	)
	public boolean chunkTickInEntityTickingChunksBackport_cancelVanillaChunkTick(ServerLevel serverLevel, LevelChunk levelChunk, int randomTickSpeed)
	{
		if (CarpetTISAdditionSettings.chunkTickInEntityTickingChunksBackport)
		{
			return false;
		}
		return true;
	}

	// Implements like 1.21.5: the chunk-ticking interation is performed after the mob-spawning iteration is done

	@Inject(
			//#if MC >= 1.21.3
			//$$ method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;JLjava/util/List;)V",
			//#else
			method = "tickChunks",
			//#endif
			at = @At(
					value = "CONSTANT",
					args = "stringValue=customSpawners"
			)
	)
	public void chunkTickInEntityTickingChunksBackport_doChunkTickForEntityTickingChunks(CallbackInfo ci)
	{
		if (!CarpetTISAdditionSettings.chunkTickInEntityTickingChunksBackport)
		{
			return;
		}

		int randomTickSpeed = this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
		for (ChunkHolder chunkHolder : ((ChunkMapAccessor)this.chunkMap).invokeGetChunks())
		{
			LevelChunk chunk = chunkHolder.getTickingChunk();
			if (chunk == null)
			{
				continue;
			}

			//#if MC >= 1.18
			//$$ long chunkPosKey = PositionUtils.packChunkPos(chunk.getPos());
			//$$ boolean isEntityTicking = this.chunkMap.getDistanceManager().inEntityTickingRange(chunkPosKey);
			//#elseif MC >= 1.17
			//$$ boolean isEntityTicking = this.level.isPositionEntityTicking(chunk.getPos());
			//#else
			boolean isEntityTicking = ((ServerChunkCache)(Object)this).isEntityTickingChunk(chunk.getPos());
			//#endif

			if (isEntityTicking)
			{
				this.level.tickChunk(chunk, randomTickSpeed);
			}
		}
	}
}
