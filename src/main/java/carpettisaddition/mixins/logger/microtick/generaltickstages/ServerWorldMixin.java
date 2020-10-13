package carpettisaddition.mixins.logger.microtick.generaltickstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.tickstages.StringTickStage;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


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
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "WorldBorder");
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickPending"
			)
	)
	private void onStageTileTick(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "TileTick");
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/RaidManager;tick()V"
			)
	)
	private void onStageRaid(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "Raid");
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
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "WanderingTrader");
	}

	@Inject(
			method = "sendBlockActions",
			at = @At("HEAD")
	)
	private void onStageBlockEvent(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "BlockEvent");
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=entities"
			)
	)
	private void onStageEntitiesWeather(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "Entity");
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new StringTickStage("Ticking weather effects")); // TISCM Micro
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
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new StringTickStage("Ticking regular entities")); // TISCM Micro
	}

	@Inject(
			method = "tickChunk",
			at = @At("HEAD")
	)
	private void onTickChunk(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage((ServerWorld)(Object)this, "RandomTick&Climate");
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
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Thunder");
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
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "Ice&Snow");
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
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, "RandomTick");
	}
}
