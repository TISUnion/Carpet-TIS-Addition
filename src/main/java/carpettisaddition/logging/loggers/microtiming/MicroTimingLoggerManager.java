package carpettisaddition.logging.loggers.microtiming;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.interfaces.IServerWorld_microTimingLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.*;
import carpettisaddition.logging.loggers.microtiming.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
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

public class MicroTimingLoggerManager
{
    private static MicroTimingLoggerManager instance;

    private final Map<ServerWorld, MicroTimingLogger> loggers = new Reference2ObjectArrayMap<>();
    private final MicroTimingLogger dummyLoggerForTranslate = new MicroTimingLogger(null);
    private long lastFlushTime;

    public MicroTimingLoggerManager(MinecraftServer minecraftServer)
    {
        this.lastFlushTime = -1;
        for (ServerWorld world : minecraftServer.getWorlds())
        {
            this.loggers.put(world, ((IServerWorld_microTimingLogger)world).getMicroTickLogger());
        }
    }

    public static MicroTimingLoggerManager getInstance()
    {
        return instance;
    }

    public Map<ServerWorld, MicroTimingLogger> getLoggers()
    {
        return loggers;
    }

    public static boolean isLoggerActivated()
    {
        return CarpetTISAdditionSettings.microTiming && ExtensionLoggerRegistry.__microTiming && instance != null;
    }

    public static void attachServer(MinecraftServer minecraftServer)
    {
        instance = new MicroTimingLoggerManager(minecraftServer);
        CarpetTISAdditionServer.LOGGER.debug("Attached MicroTick loggers to " + instance.loggers.size() + " worlds");
    }

    public static void detachServer()
    {
        instance = null;
        CarpetTISAdditionServer.LOGGER.debug("Detached MicroTick loggers");
    }

    private static Optional<MicroTimingLogger> getWorldLogger(World world)
    {
        if (instance != null && world instanceof ServerWorld)
        {
            return Optional.of(((IServerWorld_microTimingLogger)world).getMicroTickLogger());
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
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, pos, new DetectBlockUpdateEvent(eventType, fromBlock, updateType, () -> updateType.getUpdateOrderList(exceptSide)), MicroTimingUtil::getEndRodWoolColor));
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
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, event.pos, new ExecuteTileTickEvent(eventType, event)));
        }
    }

    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority, Boolean success)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, pos, new ScheduleTileTickEvent(block, pos, delay, priority, success)));
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

    public static void onExecuteBlockEvent(World world, BlockEvent blockAction, Boolean returnValue, ExecuteBlockEventEvent.FailInfo failInfo, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, blockAction.getPos(), new ExecuteBlockEventEvent(eventType, blockAction, returnValue, failInfo)));
        }
    }

    public static void onScheduleBlockEvent(World world, BlockEvent blockAction, boolean success)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, blockAction.getPos(), new ScheduleBlockEventEvent(blockAction, success)));
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
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, pos, new EmitBlockUpdateEvent(eventType, block, methodName)));
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
            for (MicroTimingLogger logger : instance.loggers.values())
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
            for (MicroTimingLogger logger : instance.loggers.values())
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

    private void flush(long gameTime) // needs to call at the end of a gt
    {
        if (gameTime != this.lastFlushTime)
        {
            this.lastFlushTime = gameTime;
            for (MicroTimingLogger logger : this.loggers.values())
            {
                logger.flushMessages();
            }
        }
    }

    public static void flushMessages(long gameTime) // needs to call at the end of a gt
    {
        if (instance != null && isLoggerActivated())
        {
            instance.flush(gameTime);
        }
    }

    public static Optional<TickStage> getTickStage(World world)
    {
        return getWorldLogger(world).map(MicroTimingLogger::getTickStage);
    }
}
