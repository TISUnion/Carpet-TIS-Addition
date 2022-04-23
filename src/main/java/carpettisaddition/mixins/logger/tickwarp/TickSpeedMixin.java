package carpettisaddition.mixins.logger.tickwarp;

import carpet.helpers.TickSpeed;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TickSpeed.class)
public abstract class TickSpeedMixin
{
	@Inject(method = "tickrate_advance", at = @At("TAIL"), remap = false)
	private static void recordTickWarpAdvancer(CallbackInfoReturnable<MutableText> cir)
	{
		TickWarpHUDLogger.getInstance().recordTickWarpAdvancer();
	}

	@Inject(method = "finish_time_warp", at = @At("HEAD"), remap = false)
	private static void recordTickWarpResult(CallbackInfo ci)
	{
		TickWarpHUDLogger.getInstance().recordTickWarpResult();
	}
}
