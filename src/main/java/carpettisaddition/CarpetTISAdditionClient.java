package carpettisaddition;

import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarpetTISAdditionClient
{
	private static final CarpetTISAdditionClient INSTANCE = new CarpetTISAdditionClient();
	public static final Logger LOGGER = LogManager.getLogger(CarpetTISAdditionMod.MOD_NAME);

	public static CarpetTISAdditionClient getInstance()
	{
		return INSTANCE;
	}

	public void onClientTick(MinecraftClient client)
	{
		ServerMsptMetricsDataSyncer.getInstance().clientTick();
	}

	public void onClientDisconnected(MinecraftClient client)
	{
		ServerMsptMetricsDataSyncer.getInstance().reset();
	}
}
