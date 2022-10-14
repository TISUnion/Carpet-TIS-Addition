package carpettisaddition.mixins.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Shadow private int ticks;

	@ModifyArg(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/MetricsData;pushSample(J)V"
			)
	)
	private long sendServerTpsMetricsData_getMsThisTick(long msThisTick)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, msThisTick);
		}
		return msThisTick;
	}
}
