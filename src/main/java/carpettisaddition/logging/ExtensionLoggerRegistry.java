package carpettisaddition.logging;

import carpet.logging.Logger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import carpettisaddition.logging.loggers.entity.ItemLogger;
import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;
import carpettisaddition.mixins.carpet.LoggerRegistryInvoker;


public class ExtensionLoggerRegistry
{
    public static boolean __ticket;
    public static boolean __memory;
    public static boolean __item;
    public static boolean __xporb;
    public static boolean __raid;
    public static boolean __microTiming;
    public static boolean __damage;
    public static boolean __commandBlock;

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
        LoggerRegistryInvoker.callRegisterLogger(MicroTimingStandardCarpetLogger.NAME, MicroTimingStandardCarpetLogger.create());
        LoggerRegistryInvoker.callRegisterLogger("damage", standardLogger("damage", "all", new String[]{"all", "players", "me"}));
        LoggerRegistryInvoker.callRegisterLogger(CommandBlockLogger.NAME, standardLogger(CommandBlockLogger.NAME, "throttled", new String[]{"throttled", "all"}));
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
