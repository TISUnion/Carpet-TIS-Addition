package carpettisaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.entity.ItemLogger;
import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueLogger;
import carpettisaddition.logging.loggers.memory.MemoryLogger;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import carpettisaddition.logging.loggers.ticket.TicketLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpLogger;


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
    public static boolean __lightQueue;
    public static boolean __tickWarp;

    public static void registerLoggers()
    {
        LoggerRegistry.registerLogger(
                TicketLogger.NAME, standardLogger("ticket", "portal", new String[]{
                        "portal,player", "portal,dragon", "start", "dragon", "player", "forced", "light", "portal", "post_teleport", "unknown"
                }
        ));
        LoggerRegistry.registerLogger(ItemLogger.getInstance().getLoggerName(), ItemLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger(XPOrbLogger.getInstance().getLoggerName(), XPOrbLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger(RaidLogger.NAME, standardLogger(RaidLogger.NAME, null, null));
        LoggerRegistry.registerLogger(MemoryLogger.NAME, standardHUDLogger(MemoryLogger.NAME, null, null));
        LoggerRegistry.registerLogger(MicroTimingStandardCarpetLogger.NAME, MicroTimingStandardCarpetLogger.create());
        LoggerRegistry.registerLogger(DamageLogger.NAME, standardLogger(DamageLogger.NAME, "all", new String[]{"all", "players", "me"}));
        LoggerRegistry.registerLogger(CommandBlockLogger.NAME, standardLogger(CommandBlockLogger.NAME, "throttled", new String[]{"throttled", "all"}));
        LoggerRegistry.registerLogger(LightQueueLogger.NAME, LightQueueLogger.getInstance().getHUDLogger());
        LoggerRegistry.registerLogger(TickWarpLogger.NAME, standardHUDLogger(TickWarpLogger.NAME, "bar", new String[]{"bar", "value"}));
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

    public static HUDLogger standardHUDLogger(String logName, String def, String [] options)
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
