package carpettisaddition.utils;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.BaseText;


public class ExtensionHUDController
{
    public static void update_hud(MinecraftServer server)
    {
        if (ExtensionLoggerRegistry.__memory)
        {
            LoggerRegistry.getLogger("memory").log(() -> send_mem_usage());
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
