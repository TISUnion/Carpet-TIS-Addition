package carpettisaddition.logging.loggers.microtiming;

import carpettisaddition.logging.loggers.microtiming.interfaces.ServerWorldWithMicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class MicroTimingAccess
{
	public static TickPhase getTickPhase(@NotNull ServerWorld world)
	{
		return ((ServerWorldWithMicroTimingLogger)world).getMicroTimingLogger().getTickPhase();
	}

	public static TickPhase getTickPhase()
	{
		ServerWorld serverWorld = MicroTimingLoggerManager.getCurrentWorld();
		if (serverWorld != null)
		{
			return getTickPhase(serverWorld);
		}
		else
		{
			return MicroTimingLoggerManager.getOffWorldTickPhase();
		}
	}
}
