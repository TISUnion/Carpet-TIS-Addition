package carpettisaddition.helpers.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.MetricsData;

/**
 * Clientside data receiver & storage
 */
public class ServerMsptMetricsDataStorage
{
	private static final ServerMsptMetricsDataStorage INSTANCE = new ServerMsptMetricsDataStorage();

	private final MetricsData metricsData;
	private boolean prevDebugTpsEnabled;

	private ServerMsptMetricsDataStorage()
	{
		this.metricsData = new MetricsData();
		this.prevDebugTpsEnabled = false;
	}

	public static ServerMsptMetricsDataStorage getInstance()
	{
		return INSTANCE;
	}

	public void receiveMetricData(long msThisTick)
	{
		this.metricsData.pushSample(msThisTick);
	}

	public MetricsData getMetricsData()
	{
		return this.metricsData;
	}

	public boolean isEnabled()
	{
		return CarpetTISAdditionSettings.syncServerMsptMetricsData && TISCMProtocol.C2S.MSPT_METRICS_SUBSCRIBE.isSupported();
	}

	public void sendMsptMetricsSubscriptionState(boolean subscribing)
	{
		TISCMClientPacketHandler.getInstance().sendPacket(
				TISCMProtocol.C2S.MSPT_METRICS_SUBSCRIBE,
				buf -> buf.writeBoolean(subscribing)
		);
	}

	public void clientTick()
	{
		boolean currentDebugTpsEnabled = MinecraftClient.getInstance().options.debugTpsEnabled;
		if (currentDebugTpsEnabled != this.prevDebugTpsEnabled)
		{
			if (this.isEnabled())
			{
				this.sendMsptMetricsSubscriptionState(currentDebugTpsEnabled);
			}
		}
		this.prevDebugTpsEnabled = currentDebugTpsEnabled;
	}
}
