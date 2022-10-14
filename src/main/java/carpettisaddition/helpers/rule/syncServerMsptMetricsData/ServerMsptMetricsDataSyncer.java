package carpettisaddition.helpers.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.MetricsData;

public class ServerMsptMetricsDataSyncer
{
	private static final ServerMsptMetricsDataSyncer INSTANCE = new ServerMsptMetricsDataSyncer();

	private MetricsData metricsData;

	private ServerMsptMetricsDataSyncer()
	{
		this.metricsData = new MetricsData();
	}

	public static ServerMsptMetricsDataSyncer getInstance()
	{
		return INSTANCE;
	}

	public void broadcastSample(long timeStamp, long msThisTick)
	{
		TISCMServerPacketHandler.getInstance().broadcast(TISCMProtocol.S2C.MSPT_METRICS_SAMPLE, nbt -> {
			nbt.putLong("time_stamp", timeStamp);
			nbt.putLong("millisecond", msThisTick);
		});
	}

	public void receiveMetricData(CompoundTag nbt)
	{
		long timeStamp = nbt.getLong("time_stamp");
		long msThisTick = nbt.getLong("millisecond");
		this.metricsData.pushSample(msThisTick);
	}

	public MetricsData getMetricsData()
	{
		return this.metricsData;
	}

	public void reset()
	{
		this.metricsData = new MetricsData();
	}

	public boolean isServerSupportOk()
	{
		return CarpetTISAdditionSettings.syncServerMsptMetricsData && TISCMClientPacketHandler.getInstance().isProtocolEnabled();
	}

	public void clientTick()
	{
		// maybe
	}
}
