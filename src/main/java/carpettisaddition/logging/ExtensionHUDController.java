package carpettisaddition.logging;

import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueLogger;
import carpettisaddition.logging.loggers.memory.MemoryLogger;
import carpettisaddition.logging.loggers.tickWarp.TickWarpLogger;
import net.minecraft.server.MinecraftServer;


public class ExtensionHUDController
{
    public static void update_hud(MinecraftServer server)
    {
        doHudLogging(ExtensionLoggerRegistry.__memory, MemoryLogger.NAME, MemoryLogger.getInstance());
        doHudLogging(ExtensionLoggerRegistry.__lightQueue, LightQueueLogger.NAME, LightQueueLogger.getInstance());
        doHudLogging(ExtensionLoggerRegistry.__tickWarp, TickWarpLogger.NAME, TickWarpLogger.getInstance());
    }

    private static void doHudLogging(boolean condition, String loggerName, AbstractHUDLogger logger)
    {
        if (condition)
        {
            LoggerRegistry.getLogger(loggerName).log(logger::onHudUpdate);
        }
    }
}
