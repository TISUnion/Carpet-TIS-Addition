package carpettisaddition.logging;

import carpet.logging.Logger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.ItemLogger;
import carpettisaddition.logging.loggers.XPOrbLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.mixins.carpet.LoggerRegistryInvoker;

import java.util.Arrays;


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
        LoggerRegistryInvoker.callRegisterLogger(
                "ticket", standardLogger("ticket", "portal", new String[]{
                        "portal,player", "portal,dragon", "start", "dragon", "player", "forced", "light", "portal", "post_teleport", "unknown"
                }
        ));
        LoggerRegistryInvoker.callRegisterLogger("item", ItemLogger.getInstance().getStandardLogger());
        LoggerRegistryInvoker.callRegisterLogger("xporb", XPOrbLogger.getInstance().getStandardLogger());
        LoggerRegistryInvoker.callRegisterLogger("raid", standardLogger("raid", null, null));
        LoggerRegistryInvoker.callRegisterLogger("memory", standardHUDLogger("memory", null, null));
        LoggerRegistryInvoker.callRegisterLogger("microTiming", standardLogger("microTiming", MicroTimingLogger.LoggingOption.DEFAULT.toString(), Arrays.stream(MicroTimingLogger.LoggingOption.values()).map(MicroTimingLogger.LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new)));
    }

    public static Logger standardLogger(String logName, String def, String[] options)
	{
        try
        {
            return new ExtensionLogger(ExtensionLoggerRegistry.class.getField("__" + logName), logName, def, options);
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
            return new ExtensionHUDLogger(ExtensionLoggerRegistry.class.getField("__" + logName), logName, def, options);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to create standard HUD logger \"%s\" @ %s", logName, CarpetTISAdditionServer.fancyName));
        }
    }
}
