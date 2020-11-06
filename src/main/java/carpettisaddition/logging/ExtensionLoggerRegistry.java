package carpettisaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.ItemLogger;
import carpettisaddition.logging.loggers.XPOrbLogger;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;


public class ExtensionLoggerRegistry
{
    public static boolean __ticket;
    public static boolean __memory;
    public static boolean __item;
    public static boolean __xporb;
    public static boolean __raid;
    public static boolean __microTiming;

    public static void registerLoggers()
    {
        LoggerRegistry.registerLogger(
                "ticket", standardLogger("ticket", "portal", new String[]{
                        "portal,player", "portal,dragon", "start", "dragon", "player", "forced", "light", "portal", "post_teleport", "unknown"
                }
        ));
        LoggerRegistry.registerLogger("item", ItemLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger("xporb", XPOrbLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger("raid", standardLogger("raid", null, null));
        LoggerRegistry.registerLogger("memory", standardHUDLogger("memory", null, null));
        LoggerRegistry.registerLogger(MicroTimingStandardCarpetLogger.NAME, MicroTimingStandardCarpetLogger.create());
    }

    public static Logger standardLogger(String logName, String def, String[] options)
	{
        try
        {
            return new Logger(ExtensionLoggerRegistry.class.getField("__" + logName), logName, def, options);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to create standard logger \"%s\" @ %s", logName, CarpetTISAdditionServer.fancyName));
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
            throw new RuntimeException(String.format("Failed to create standard HUD logger \"%s\" @ %s", logName, CarpetTISAdditionServer.fancyName));
        }
    }
}
