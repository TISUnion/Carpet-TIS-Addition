package carpettisaddition.helpers.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
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

	public static void apply()
	{
		//#if MC <= 11600
		//$$ setSize(CarpetTISAdditionSettings.lightEngineMaxBatchSize);
		//#endif
	}
}
