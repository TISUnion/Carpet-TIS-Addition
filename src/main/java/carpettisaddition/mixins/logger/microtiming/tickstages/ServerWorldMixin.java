package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickstages.BlockEventTickStageExtra;
import carpettisaddition.logging.loggers.microtiming.tickstages.StringTickStageExtra;
import carpettisaddition.logging.loggers.microtiming.tickstages.TileTickTickStageExtra;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/border/WorldBorder;tick()V"
			)
	)
	private void onStageWorldBorder(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.WORLD_BORDER);
	}

	/*
	 * ------------------
	 *  Tile Tick starts
	 * ------------------
	 */

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickPending"
			)
	)
	private void onStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.TILE_TICK);
	}

	private int tileTickOrderCounter = 0;

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickPending"
			)
	)
	private void onEnterTileTickStage(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(method = "tickBlock", at = @At("HEAD"))
	private void beforeExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(event.priority.getIndex()));
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new TileTickTickStageExtra((ServerWorld)(Object)this, event, this.tileTickOrderCounter++));
	}

	@Inject(method = "tickBlock", at = @At("RETURN"))
	private void afterExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, null);
	}

	/*
	 * ----------------
	 *  Tile Tick ends
	 * ----------------
	 */

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/RaidManager;tick()V"
			)
	)
	private void onStageRaid(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.RAID);
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/WanderingTraderManager;tick()V"
			)
	)
	void onStageWanderingTrader(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.WANDERING_TRADER);
	}

	/*
	 * --------------------
	 *  Block Event starts
	 * --------------------
	 */

	@Inject(
			method = "sendBlockActions",
			at = @At("HEAD")
	)
	private void onStageBlockEvent(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.BLOCK_EVENT);
	}

	@Shadow
	@Final
	private ObjectLinkedOpenHashSet<BlockAction> pendingBlockActions;

	private int blockEventOrderCounter;
	private int blockEventDepth;
	private int blockEventCurrentDepthCounter;
	private int blockEventCurrentDepthSize;

	@Inject(
			method = "sendBlockActions",
			at = @At("HEAD")
	)
	private void onEnterBlockEventStage(CallbackInfo ci)
	{
		this.blockEventOrderCounter = 0;
		this.blockEventCurrentDepthCounter = 0;
		this.blockEventDepth = 0;
		this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
	}

	@Inject(method = "method_14174", at = @At("HEAD"))
	private void beforeBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(this.blockEventDepth));
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new BlockEventTickStageExtra((ServerWorld)(Object)this, blockAction, this.blockEventOrderCounter++, this.blockEventDepth));
	}

	@Inject(method = "method_14174", at = @At("RETURN"))
	private void afterBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, null);
		this.blockEventCurrentDepthCounter++;
		if (this.blockEventCurrentDepthCounter == this.blockEventCurrentDepthSize)
		{
			this.blockEventDepth++;
			this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
			this.blockEventCurrentDepthCounter = 0;
		}
	}

	/*
	 * ------------------
	 *  Block Event ends
	 * ------------------
	 */

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=entities"
			)
	)
	private void onStageEntitiesWeather(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.ENTITY);
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, StringTickStageExtra.ENTITY_WEATHER_EFFECT);
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=regular"
			)
	)
	private void onStageEntitiesRegular(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageExtra((ServerWorld)(Object)this, StringTickStageExtra.ENTITY_REGULAR);
	}

	@Inject(
			method = "tickChunk",
			at = @At("HEAD")
	)
	private void onTickChunk(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.CHUNK_TICK);
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=thunder"
			)
	)
	private void onStageDetailThunder(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Thunder");
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=iceandsnow"
			)
	)
	private void onStageDetailIceAndSnow(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Ice&Snow");
	}

	@Inject(
			method = "tickChunk",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickBlocks"
			)
	)
	private void onStageDetailRandomTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "RandomTick");
	}
}
