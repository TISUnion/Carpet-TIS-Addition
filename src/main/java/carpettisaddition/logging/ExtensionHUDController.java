package carpettisaddition.logging;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueLogger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.BaseText;


public class ExtensionHUDController
{
    public static void update_hud(MinecraftServer server)
    {
        if (ExtensionLoggerRegistry.__memory)
        {
            LoggerRegistry.getLogger("memory").log(ExtensionHUDController::send_mem_usage);
        }
        if (ExtensionLoggerRegistry.__lightQueue)
        {
            LoggerRegistry.getLogger(LightQueueLogger.NAME).log((option, player) -> LightQueueLogger.getInstance().onHudUpdate(option, player));
        }
    }

    private static BaseText [] send_mem_usage()
    {
        final long bytesPerMB = 1048576;
        long occupiedMemoryMB = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / bytesPerMB;
        long totalMemoryMB = Runtime.getRuntime().maxMemory() / bytesPerMB;
        return new BaseText[]{
                Messenger.c(String.format("g %dM / %dM", occupiedMemoryMB, totalMemoryMB))
        };
    }
}
