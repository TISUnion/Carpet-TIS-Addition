package carpettisaddition.helpers.rule.syncServerMsptMetricsData;

import carpettisaddition.network.TISCMProtocol;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Serverside data syncer
 */
public class ServerMsptMetricsDataSyncer
{
	private static final ServerMsptMetricsDataSyncer INSTANCE = new ServerMsptMetricsDataSyncer();

	private final Set<ServerPlayNetworkHandler> clientsToSync = Collections.newSetFromMap(new WeakHashMap<>());

	private ServerMsptMetricsDataSyncer() {}

	public static ServerMsptMetricsDataSyncer getInstance()
	{
		return INSTANCE;
	}

	public void addClient(ServerPlayNetworkHandler networkHandler)
	{
		this.clientsToSync.add(networkHandler);
	}

	public void removeClient(ServerPlayNetworkHandler networkHandler)
	{
		this.clientsToSync.remove(networkHandler);
	}

	public void broadcastSample(long msThisTick)
	{
		this.clientsToSync.forEach(networkHandler ->
			networkHandler.sendPacket(TISCMProtocol.S2C.MSPT_METRICS_SAMPLE.packet(buf -> buf.
					writeLong(msThisTick)
			))
		);
	}
}
