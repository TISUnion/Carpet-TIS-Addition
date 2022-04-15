package carpettisaddition.logging;

import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.logging.loggers.lifetime.LifeTimeHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.memory.MemoryHUDLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import net.minecraft.server.MinecraftServer;

public class TISAdditionHUDController
{
    public static void updateHUD(MinecraftServer server)
    {
        doHudLogging(TISAdditionLoggerRegistry.__lightQueue, LightQueueHUDLogger.NAME, LightQueueHUDLogger.getInstance());
        doHudLogging(TISAdditionLoggerRegistry.__lifeTime, LifeTimeHUDLogger.NAME, LifeTimeHUDLogger.getInstance());
        doHudLogging(TISAdditionLoggerRegistry.__tickWarp, TickWarpHUDLogger.NAME, TickWarpHUDLogger.getInstance());
        doHudLogging(TISAdditionLoggerRegistry.__memory, MemoryHUDLogger.NAME, MemoryHUDLogger.getInstance());
    }

    public static void doHudLogging(boolean condition, String loggerName, AbstractHUDLogger logger)
    {
        if (condition)
        {
            LoggerRegistry.getLogger(loggerName).log(logger::onHudUpdate);
        }
    }
}
