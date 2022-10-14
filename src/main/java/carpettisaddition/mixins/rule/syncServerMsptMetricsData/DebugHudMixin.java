package carpettisaddition.mixins.rule.syncServerMsptMetricsData;

import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
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

//#if MC >= 11600
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

@Mixin(DebugHud.class)
public abstract class DebugHudMixin
{
	@Shadow protected abstract void drawMetricsData(
			//#if MC >= 11600
			//$$ MatrixStack matrixStack,
			//#endif
			MetricsData metricsData, int startY, int firstSample, boolean isClient
	);

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/client/MinecraftClient;getServer()Lnet/minecraft/server/integrated/IntegratedServer;",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void syncServerMsptMetricsData_drawIfPossible(
			//#if MC >= 11600
			//$$ MatrixStack matrixStack,
			//#endif
			CallbackInfo ci,
			Entity entity, int i, IntegratedServer integratedServer
	)
	{
		if (integratedServer == null)
		{
			if (ServerMsptMetricsDataSyncer.getInstance().isServerSupportOk())
			{
				MetricsData metricsData = ServerMsptMetricsDataSyncer.getInstance().getMetricsData();

				// vanilla copy
				this.drawMetricsData(
						//#if MC >= 11600
						//$$ matrixStack,
						//#endif
						metricsData, i - Math.min(i / 2, 240), i / 2, false
				);
			}
		}
	}
}
