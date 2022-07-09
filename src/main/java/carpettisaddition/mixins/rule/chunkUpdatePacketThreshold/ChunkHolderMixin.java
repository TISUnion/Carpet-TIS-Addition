package carpettisaddition.mixins.rule.chunkUpdatePacketThreshold;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.google.common.collect.Sets;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin
{
	//#if MC < 11600

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

	@ModifyConstant(
			method = {"markForBlockUpdate", "flushUpdates"},
			constant = @Constant(intValue = 64),
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
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void useSetToStoreUpdatePos(int x, int y, int z, CallbackInfo ci, short s, int i)
	{
		if (this.ruleEnabled$CUPT)
		{
			this.blockUpdatePositionsSet$CUPT.add(s);
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

	//#endif
}
