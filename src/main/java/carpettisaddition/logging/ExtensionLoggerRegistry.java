package carpettisaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;


public class ExtensionLoggerRegistry
{
    public static boolean __ticket;
    public static boolean __memory;
    /*
    public static void registerLoggers()
    {
        LoggerRegistry.registerLogger(
                "ticket", standardLogger("ticket", "portal", new String[]{
                        "portal,player", "portal,dragon", "start", "dragon", "player", "forced", "light", "portal", "post_teleport", "unknown"
                }
        ));
        LoggerRegistry.registerLogger("memory", standardHUDLogger("memory", null, null));
    }

    private static Logger standardLogger(String logName, String def, String[] options)
	{
        try
        {
            return new Logger(ExtensionLoggerRegistry.class.getField("__" + logName), logName, def, options);
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
            return new HUDLogger(ExtensionLoggerRegistry.class.getField("__" + logName), logName, def, options);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to create standard HUD logger %s carpet TIS addition", logName));
        }
    }
    */
}
