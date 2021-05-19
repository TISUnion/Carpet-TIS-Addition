package carpettisaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.entity.ItemLogger;
import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import carpettisaddition.logging.loggers.lifetime.LifeTimeHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.memory.MemoryHUDLogger;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import carpettisaddition.logging.loggers.ticket.TicketLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;

import java.lang.reflect.Field;


public class TISAdditionLoggerRegistry
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
    public static boolean __turtleEgg;
    public static boolean __lifeTime;

    public static void registerLoggers()
    {
        LoggerRegistry.registerLogger(TicketLogger.NAME, TicketLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger(ItemLogger.getInstance().getLoggerName(), ItemLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger(XPOrbLogger.getInstance().getLoggerName(), XPOrbLogger.getInstance().getStandardLogger());
        LoggerRegistry.registerLogger(RaidLogger.NAME, standardLogger(RaidLogger.NAME, null, null));
        LoggerRegistry.registerLogger(MemoryHUDLogger.NAME, standardHUDLogger(MemoryHUDLogger.NAME, null, null));
        LoggerRegistry.registerLogger(MicroTimingStandardCarpetLogger.NAME, MicroTimingStandardCarpetLogger.getInstance());
        LoggerRegistry.registerLogger(DamageLogger.NAME, standardLogger(DamageLogger.NAME, "all", new String[]{"all", "players", "me"}));
        LoggerRegistry.registerLogger(CommandBlockLogger.NAME, standardLogger(CommandBlockLogger.NAME, "throttled", new String[]{"throttled", "all"}));
        LoggerRegistry.registerLogger(LightQueueHUDLogger.NAME, LightQueueHUDLogger.getInstance().getHUDLogger());
        LoggerRegistry.registerLogger(TickWarpHUDLogger.NAME, standardHUDLogger(TickWarpHUDLogger.NAME, "bar", new String[]{"bar", "value"}));
        LoggerRegistry.registerLogger(TurtleEggLogger.NAME, standardLogger(TurtleEggLogger.NAME, null, null));
        LoggerRegistry.registerLogger(LifeTimeHUDLogger.NAME, LifeTimeHUDLogger.getInstance().getHUDLogger());
    }

    public static Field getLoggerField(String logName)
    {
        try
        {
            return TISAdditionLoggerRegistry.class.getField("__" + logName);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(String.format("Failed to get logger field \"%s\" @ %s", logName, CarpetTISAdditionServer.fancyName));
        }
    }

    public static Logger standardLogger(String logName, String def, String[] options)
	{
        return new Logger(getLoggerField(logName), logName, def, options);
    }

    public static HUDLogger standardHUDLogger(String logName, String def, String [] options)
    {
        return new HUDLogger(getLoggerField(logName), logName, def, options);
    }
}
