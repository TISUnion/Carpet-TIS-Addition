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
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin
{
	@Mutable
	@Shadow @Final private short[] blockUpdatePositions;

	@Shadow private int blockUpdateCount;

	// CUPT = chunkUpdatePacketThreshold
	private boolean ruleEnabled$CUPT;
	private int ruleValue$CUPT;
	private final Set<Short> blockUpdatePositionsSet$CUPT = Sets.newLinkedHashSet();

	private void updateRuleStatus$CUPT()
	{
		this.ruleValue$CUPT = CarpetTISAdditionSettings.chunkUpdatePacketThreshold;
		this.ruleEnabled$CUPT = this.ruleValue$CUPT != CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD;
		// rollback to vanilla array when rule disabled and array length is incorrect
		if (!this.ruleEnabled$CUPT && this.blockUpdatePositions.length != CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD)
		{
			this.blockUpdatePositions = new short[CarpetTISAdditionSettings.VANILLA_CHUNK_UPDATE_PACKET_THRESHOLD];
		}
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void updateRuleStatusAfterInit$CUPT(CallbackInfo ci)
	{
		this.updateRuleStatus$CUPT();
	}

	@ModifyExpressionValue(
			method = {"markForBlockUpdate", "flushUpdates"},
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
			method = "markForBlockUpdate",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/world/ChunkHolder;blockUpdateCount:I",
					ordinal = 1
			),
			cancellable = true
	)
	private void useSetToStoreUpdatePos(int x, int y, int z, CallbackInfo ci, @Local short packedPos)
	{
		if (this.ruleEnabled$CUPT)
		{
			this.blockUpdatePositionsSet$CUPT.add(packedPos);
			this.blockUpdateCount = this.blockUpdatePositionsSet$CUPT.size();
			ci.cancel();
		}
	}

	@Inject(
			method = "flushUpdates",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/chunk/WorldChunk;getWorld()Lnet/minecraft/world/World;"
			)
	)
	private void flushUpdatePosFromSet(WorldChunk worldChunk, CallbackInfo ci)
	{
		if (this.ruleEnabled$CUPT)
		{
			this.blockUpdatePositions = new short[this.blockUpdatePositionsSet$CUPT.size()];
			int i = 0;
			for (short s : this.blockUpdatePositionsSet$CUPT)
			{
				this.blockUpdatePositions[i++] = s;
			}
			this.blockUpdatePositionsSet$CUPT.clear();
		}
	}

	@Inject(method = "flushUpdates", at = @At("TAIL"))
	private void recheckRuleChunkUpdatePacketThresholdIfEnabled(WorldChunk worldChunk, CallbackInfo ci)
	{
		this.updateRuleStatus$CUPT();
	}
}
