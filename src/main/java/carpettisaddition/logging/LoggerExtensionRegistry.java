package carpettisaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;

public class LoggerExtensionRegistry
{
    public static boolean __ticket;

    public static void registerLoggers()
    {
        LoggerRegistry.registerLogger(
                "ticket", standardLogger("ticket", "portal", new String[]{
                "portal,dragon", "start", "dragon", "player", "forced", "light", "portal", "post_teleport", "unknown"
        }));
    }

    private static Logger standardLogger(String logName, String def, String[] options)
	{
        try
        {
            return new Logger(LoggerExtensionRegistry.class.getField("__" + logName), logName, def, options);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to create standard logger %s carpet TIS addition", logName));
        }
    }

    private static Logger standardHUDLogger(String logName, String def, String [] options)
    {
        try
        {
            return new HUDLogger(LoggerExtensionRegistry.class.getField("__"+logName), logName, def, options);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to create standard HUD logger %s carpet TIS addition", logName));
        }
    }
}
