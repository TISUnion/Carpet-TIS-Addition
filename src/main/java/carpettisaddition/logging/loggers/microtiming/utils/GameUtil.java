package carpettisaddition.logging.loggers.microtiming.utils;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.world.dimension.DimensionType;

public class GameUtil
{
	public static long getGameTime()
	{
		return CarpetTISAdditionServer.minecraft_server.getWorld(DimensionType.OVERWORLD).getTime();
	}
}
