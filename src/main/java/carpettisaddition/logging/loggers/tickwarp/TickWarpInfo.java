package carpettisaddition.logging.loggers.tickwarp;

import carpet.helpers.TickSpeed;
import net.minecraft.server.network.ServerPlayerEntity;

public class TickWarpInfo
{
	public boolean isWarping()
	{
		return TickSpeed.time_bias > 0;
	}

	public long getTotalTicks()
	{
		return TickSpeed.time_warp_scheduled_ticks;
	}

	public long getRemainingTicks()
	{
		return TickSpeed.time_bias;
	}

	public long getStartTime()
	{
		return TickSpeed.time_warp_start_time;
	}

	public ServerPlayerEntity getTimeAdvancer()
	{
		return TickSpeed.time_advancerer instanceof ServerPlayerEntity ? (ServerPlayerEntity)TickSpeed.time_advancerer : null;
	}

	public long getCurrentTime()
	{
		return System.nanoTime();
	}
}
