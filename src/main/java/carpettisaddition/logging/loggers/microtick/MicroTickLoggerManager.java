package carpettisaddition.logging.loggers.microtick;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.interfaces.IServerWorld_MicroTickLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import carpettisaddition.logging.loggers.microtick.enums.TickStage;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStageExtraBase;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class MicroTickLoggerManager
{
    private static MicroTickLoggerManager instance;

    private final Map<ServerWorld, MicroTickLogger> loggers = new Reference2ObjectArrayMap<>();
    private final MicroTickLogger dummyLoggerForTranslate = new MicroTickLogger(null);

    public MicroTickLoggerManager(MinecraftServer minecraftServer)
    {
        for (ServerWorld world : minecraftServer.getWorlds())
        {
            this.loggers.put(world, ((IServerWorld_MicroTickLogger)world).getMicroTickLogger());
        }
    }

    public static boolean isLoggerActivated()
    {
        return CarpetTISAdditionSettings.microTick && ExtensionLoggerRegistry.__microtick && instance != null;
    }

    public static void attachServer(MinecraftServer minecraftServer)
    {
        instance = new MicroTickLoggerManager(minecraftServer);
        CarpetTISAdditionServer.LOGGER.debug("Attached MicroTick loggers to " + instance.loggers.size() + " worlds");
    }

    public static void detachServer()
    {
        instance = null;
        CarpetTISAdditionServer.LOGGER.debug("Detached MicroTick loggers");
    }

    private static Optional<MicroTickLogger> getWorldLogger(World world)
    {
        if (instance != null && world instanceof ServerWorld)
        {
            return Optional.of(((IServerWorld_MicroTickLogger)world).getMicroTickLogger());
        }
        return Optional.empty();
    }

    public static String tr(String key, String text, boolean autoFormat)
    {
        return instance.dummyLoggerForTranslate.tr(key, text, autoFormat);
    }

    public static String tr(String key, String text)
    {
        return instance.dummyLoggerForTranslate.tr(key, text);
    }

    public static String tr(String key)
    {
        return instance.dummyLoggerForTranslate.tr(key);
    }

    /*
     * ----------------------------------
     *  Block Update and Block Operation
     * ----------------------------------
     */

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onBlockUpdate(world, pos, fromBlock, updateType, () -> updateType.getUpdateOrderList(exceptSide), eventType));
        }
    }

    public static void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, EventType eventType)
    {
        if (isLoggerActivated())
        {
            if (oldState.getBlock() == newState.getBlock())
            {
                getWorldLogger(world).ifPresent(logger -> logger.onSetBlockState(world, pos, oldState, newState, returnValue, eventType));
            }
        }
    }

    /*
     * -----------
     *  Tile Tick
     * -----------
     */

    public static void onExecuteTileTickEvent(World world, ScheduledTick<Block> event, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onExecuteTileTick(world, event, eventType));
        }
    }

    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority, Boolean success)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onScheduleTileTick(world, block, pos, delay, priority, success));
        }
    }
    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority)
    {
        onScheduleTileTickEvent(world, block, pos, delay, priority, null);
    }
    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay)
    {
        onScheduleTileTickEvent(world, block, pos, delay, TickPriority.NORMAL);
    }

    /*
     * -------------
     *  Block Event
     * -------------
     */

    public static void onExecuteBlockEvent(World world, BlockEvent blockAction, Boolean returnValue, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onExecuteBlockEvent(world, blockAction, returnValue, eventType));
        }
    }

    public static void onScheduleBlockEvent(World world, BlockEvent blockAction)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onScheduleBlockEvent(world, blockAction));
        }
    }

    /*
     * ------------------
     *  Component things
     * ------------------
     */

    public static void onEmitBlockUpdate(World world, Block block, BlockPos pos, EventType eventType, String methodName)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onEmitBlockUpdate(world, block, pos, eventType, methodName));
        }
    }

    /*
     * ------------
     *  Tick Stage
     * ------------
     */

    public static void setTickStage(World world, TickStage stage)
    {
        getWorldLogger(world).ifPresent(logger -> logger.setTickStage(stage));
    }
    public static void setTickStage(TickStage stage)
    {
        if (instance != null)
        {
            for (MicroTickLogger logger : instance.loggers.values())
            {
                logger.setTickStage(stage);
            }
        }
    }

    public static void setTickStageDetail(World world, String detail)
    {
        getWorldLogger(world).ifPresent(logger -> logger.setTickStageDetail(detail));
    }

    public static void setTickStageExtra(World world, TickStageExtraBase stage)
    {
        getWorldLogger(world).ifPresent(logger -> logger.setTickStageExtra(stage));
    }
    public static void setTickStageExtra(TickStageExtraBase stage)
    {
        if (instance != null)
        {
            for (MicroTickLogger logger : instance.loggers.values())
            {
                logger.setTickStageExtra(stage);
            }
        }
    }

    /*
     * ------------
     *  Interfaces
     * ------------
     */

    public static void flushMessages() // needs to call at the end of a gt
    {
        if (instance != null && isLoggerActivated())
        {
            for (MicroTickLogger logger : instance.loggers.values())
            {
                logger.flushMessages();
            }
        }
    }

    public static Optional<TickStage> getTickStage(World world)
    {
        return getWorldLogger(world).map(MicroTickLogger::getTickStage);
    }
}
