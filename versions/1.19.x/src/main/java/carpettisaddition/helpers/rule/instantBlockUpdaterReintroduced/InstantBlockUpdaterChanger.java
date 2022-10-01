package carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class InstantBlockUpdaterChanger
{
	public static void apply(boolean useInstant)
	{
		MinecraftServer server =  CarpetTISAdditionServer.minecraft_server;
		if (server != null)
		{
			server.execute(() -> {
				for (ServerWorld world : server.getWorlds())
				{
					((NeighborUpdaterChangeableWorld)world).setUseInstantNeighborUpdater$TISCM(useInstant);
				}
			});
		}
	}

	public static void apply()
	{
		apply(CarpetTISAdditionSettings.instantBlockUpdaterReintroduced);
	}
}
