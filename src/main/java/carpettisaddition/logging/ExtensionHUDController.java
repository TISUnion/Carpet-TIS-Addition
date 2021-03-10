package carpettisaddition.logging;

import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.logging.loggers.lifetime.LifeTimeHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.memory.MemoryHUDLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import net.minecraft.server.MinecraftServer;


public class ExtensionHUDController
{
    public static void update_hud(MinecraftServer server)
    {
        doHudLogging(ExtensionLoggerRegistry.__lightQueue, LightQueueHUDLogger.NAME, LightQueueHUDLogger.getInstance());
        doHudLogging(ExtensionLoggerRegistry.__lifeTime, LifeTimeHUDLogger.NAME, LifeTimeHUDLogger.getInstance());
        doHudLogging(ExtensionLoggerRegistry.__tickWarp, TickWarpHUDLogger.NAME, TickWarpHUDLogger.getInstance());
        doHudLogging(ExtensionLoggerRegistry.__memory, MemoryHUDLogger.NAME, MemoryHUDLogger.getInstance());
    }

    private static void doHudLogging(boolean condition, String loggerName, AbstractHUDLogger logger)
    {
        if (condition)
        {
            LoggerRegistry.getLogger(loggerName).log(logger::onHudUpdate);
        }
    }
}
