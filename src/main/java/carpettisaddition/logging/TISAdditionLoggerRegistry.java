package carpettisaddition.logging;

//#if MC >= 11500
import carpet.logging.LoggerRegistry;
//#else
//$$ import carpettisaddition.logging.compat.ExtensionHUDLogger;
//$$ import carpettisaddition.logging.compat.ExtensionLogger;
//$$ import carpettisaddition.logging.compat.LoggerRegistry;
//#endif

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.entity.ItemLogger;
import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import carpettisaddition.logging.loggers.lifetime.LifeTimeHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.memory.MemoryHUDLogger;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;
import carpettisaddition.logging.loggers.movement.MovementLogger;
import carpettisaddition.logging.loggers.phantom.PhantomLogger;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import carpettisaddition.logging.loggers.scounter.SupplierCounterHUDLogger;
import carpettisaddition.logging.loggers.ticket.TicketLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;

import java.lang.reflect.Field;

public class TISAdditionLoggerRegistry
{
    public static boolean __commandBlock;
    public static boolean __damage;
    public static boolean __item;
    public static boolean __lifeTime;
    public static boolean __lightQueue;
    public static boolean __memory;
    public static boolean __microTiming;
    public static boolean __movement;
    public static boolean __phantom;
    public static boolean __raid;
    public static boolean __scounter;
    public static boolean __ticket;
    public static boolean __tickWarp;
    public static boolean __turtleEgg;
    public static boolean __xporb;

    public static void registerLoggers()
    {
        register(CommandBlockLogger.getInstance());
        register(DamageLogger.getInstance());
        register(ItemLogger.getInstance());
        register(LifeTimeHUDLogger.getInstance());
        register(LightQueueHUDLogger.getInstance());
        register(MemoryHUDLogger.getInstance());
        register(MicroTimingStandardCarpetLogger.getInstance());
        register(MovementLogger.getInstance());
        register(PhantomLogger.getInstance());
        register(RaidLogger.getInstance());
        register(SupplierCounterHUDLogger.getInstance());
        register(TicketLogger.getInstance());
        register(TickWarpHUDLogger.getInstance());
        register(TurtleEggLogger.getInstance());
        register(XPOrbLogger.getInstance());
    }

    private static void register(AbstractLogger logger)
    {
        register(logger.createCarpetLogger());
    }

    private static void register(Logger logger)
    {
        LoggerRegistry.registerLogger(logger.getLogName(), logger);
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
        return new
                //#if MC >= 11500
                Logger
                //#else
                //$$ ExtensionLogger
                //#endif
                (getLoggerField(logName), logName, def, options);
    }

    public static HUDLogger standardHUDLogger(String logName, String def, String [] options)
    {
        return new
                //#if MC >= 11500
                HUDLogger
                //#else
                //$$ ExtensionHUDLogger
                //#endif
                (getLoggerField(logName), logName, def, options);
    }
}
