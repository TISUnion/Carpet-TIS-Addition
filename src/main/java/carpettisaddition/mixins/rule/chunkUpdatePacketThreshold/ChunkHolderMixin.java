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

package carpettisaddition.mixins.rule.chunkUpdatePacketThreshold;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin
{
	@Mutable
	@Shadow @Final private short[] changedBlocks;

	@Shadow private int changes;

	// CUPT = chunkUpdatePacketThreshold
	@Unique private boolean ruleEnabled$CUPT;
	@Unique private int ruleValue$CUPT;
	@Unique private final Set<Short> blockUpdatePositionsSet$CUPT = Sets.newLinkedHashSet();

	@Unique
	private void updateRuleStatus$CUPT()
	{
		this.ruleValue$CUPT = CarpetTISAdditionSettings.chunkUpdatePacketThreshold;
		this.ruleEnabled$CUPT = this.ruleValue$CUPT != CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD;
		// rollback to vanilla array when rule disabled and array length is incorrect
		if (!this.ruleEnabled$CUPT && this.changedBlocks.length != CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD)
		{
			this.changedBlocks = new short[CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD];
		}
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void updateRuleStatusAfterInit$CUPT(CallbackInfo ci)
	{
		this.updateRuleStatus$CUPT();
	}

	@ModifyExpressionValue(
			method = {"blockChanged", "broadcastChanges"},
			at = @At(
					value = "CONSTANT",
					args = "intValue=64"
			),
			expect = 3
	)
	private int modifyChunkUpdatePacketThreshold(int value)
	{
		if (this.ruleEnabled$CUPT)
		{
			return this.ruleValue$CUPT;
		}
		return value;
	}

	@Inject(
			method = "blockChanged",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/level/ChunkHolder;changes:I",
					ordinal = 1
			),
			cancellable = true
	)
	private void useSetToStoreUpdatePos(int x, int y, int z, CallbackInfo ci, @Local short packedPos)
	{
		if (this.ruleEnabled$CUPT)
		{
			this.blockUpdatePositionsSet$CUPT.add(packedPos);
			this.changes = this.blockUpdatePositionsSet$CUPT.size();
			ci.cancel();
		}
	}

	@Inject(
			method = "broadcastChanges",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/chunk/LevelChunk;getLevel()Lnet/minecraft/world/level/Level;"
			)
	)
	private void flushUpdatePosFromSet(LevelChunk worldChunk, CallbackInfo ci)
	{
		if (this.ruleEnabled$CUPT)
		{
			this.changedBlocks = new short[this.blockUpdatePositionsSet$CUPT.size()];
			int i = 0;
			for (short s : this.blockUpdatePositionsSet$CUPT)
			{
				this.changedBlocks[i++] = s;
			}
			this.blockUpdatePositionsSet$CUPT.clear();
		}
	}

	@Inject(method = "broadcastChanges", at = @At("TAIL"))
	private void recheckRuleChunkUpdatePacketThresholdIfEnabled(LevelChunk worldChunk, CallbackInfo ci)
	{
		this.updateRuleStatus$CUPT();
	}
}
