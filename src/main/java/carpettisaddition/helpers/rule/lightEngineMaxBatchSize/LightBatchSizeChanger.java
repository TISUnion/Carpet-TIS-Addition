package carpettisaddition.helpers.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.world.ServerWorld;

public class LightBatchSizeChanger
{
	public static void setSize(int newSize)
	{
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			for (ServerWorld serverWorld : CarpetTISAdditionServer.minecraft_server.getWorlds())
			{
				serverWorld.getChunkManager().getLightingProvider().setTaskBatchSize(newSize);
			}
		}
	}
}
