package carpettisaddition.mixins.rule.syncServerMsptMetricsData;

import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataStorage;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin
{
	@Shadow protected abstract void drawMetricsData(MetricsData metricsData, int startY, int firstSample, boolean isClient);

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/client/MinecraftClient;getServer()Lnet/minecraft/server/integrated/IntegratedServer;",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void syncServerMsptMetricsData_drawIfPossible(CallbackInfo ci, Entity entity, int i, IntegratedServer integratedServer)
	{
		if (integratedServer == null)
		{
			if (ServerMsptMetricsDataStorage.getInstance().isEnabled())
			{
				MetricsData metricsData = ServerMsptMetricsDataStorage.getInstance().getMetricsData();
				// vanilla copy
				this.drawMetricsData(metricsData, i - Math.min(i / 2, 240), i / 2, false);
			}


			// TODO: modify text -> TPS (server)
		}
	}
}
