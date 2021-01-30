package carpettisaddition.logging.loggers.microtiming;

import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.*;
import carpettisaddition.logging.loggers.microtiming.interfaces.IServerWorld;
import carpettisaddition.logging.loggers.microtiming.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.script.BlockEvents;
import carpettisaddition.translations.Translator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MicroTimingLoggerManager
{
    private static MicroTimingLoggerManager instance;

    public static Set<BlockPos> trackedPositions = new HashSet<>();

    private final Map<ServerWorld, MicroTimingLogger> loggers = new Reference2ObjectArrayMap<>();
    private static final Translator TRANSLATOR = (new MicroTimingLogger(null)).getTranslator();
    private long lastFlushTime;

    public MicroTimingLoggerManager(MinecraftServer minecraftServer)
    {
        this.lastFlushTime = -1;
        for (ServerWorld world : minecraftServer.getWorlds())
        {
            this.loggers.put(world, ((IServerWorld)world).getMicroTimingLogger());
        }
    }

    public static MicroTimingLoggerManager getInstance()
    {
        return instance;
    }

    public Map<ServerWorld, MicroTimingLogger> getLoggers()
    {
        return this.loggers;
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
            return Optional.of(((IServerWorld)world).getMicroTimingLogger());
        }
        return Optional.empty();
    }

    public static String tr(String key, String text, boolean autoFormat)
    {
        return TRANSLATOR.tr(key, text, autoFormat);
    }

    public static String tr(String key, String text)
    {
        return TRANSLATOR.tr(key, text);
    }

    public static String tr(String key)
    {
        return TRANSLATOR.tr(key);
    }

    /*
     * -------------------------
     *  General Event Operation
     * -------------------------
     */

    public static void onEvent(World world, BlockPos pos, Supplier<BaseEvent> supplier, BiFunction<World, BlockPos, Optional<DyeColor>> woolGetter)
    {
        if(trackedPositions.contains(pos))//For scarpet, checking if it's a block tracked by the scarpet bit. Separate from the rest cos idk how to works
            BlockEvents.determineBlockEvent(supplier.get(), pos);

        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, pos, supplier.get(), woolGetter));
        }
    }

    public static void onEvent(World world, BlockPos pos, Supplier<BaseEvent> supplier)
    {
        if(trackedPositions.contains(pos))//For scarpet, checking if it's a block tracked by the scarpet bit. Separate from the rest cos idk how to works
            BlockEvents.determineBlockEvent(supplier.get(), pos);

        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.addMessage(world, pos, supplier.get()));
        }
    }

    /*
     * ----------------------------------
     *  Block Update and Block Operation
     * ----------------------------------
     */

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
    {
        onEvent(world, pos, () -> new DetectBlockUpdateEvent(eventType, fromBlock, updateType, () -> updateType.getUpdateOrderList(exceptSide)), MicroTimingUtil::getEndRodWoolColor);
    }

    public static void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, int flags, EventType eventType)
    {
        if (isLoggerActivated())
        {
            if (oldState.getBlock() == newState.getBlock())
            {
                getWorldLogger(world).ifPresent(logger -> logger.onSetBlockState(world, pos, oldState, newState, returnValue, flags, eventType));
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
        onEvent(world, event.pos, () -> new ExecuteTileTickEvent(eventType, event));
    }

    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority, Boolean success)
    {
        onEvent(world, pos, () -> new ScheduleTileTickEvent(block, pos, delay, priority, success));
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
        onEvent(world, blockAction.getPos(), () -> new ExecuteBlockEventEvent(eventType, blockAction, returnValue, failInfo));
    }

    public static void onScheduleBlockEvent(World world, BlockEvent blockAction, boolean success)
    {
        onEvent(world, blockAction.getPos(), () -> new ScheduleBlockEventEvent(blockAction, success));
    }

    /*
     * ------------------
     *  Component things
     * ------------------
     */

    public static void onEmitBlockUpdate(World world, Block block, BlockPos pos, EventType eventType, String methodName)
    {
        onEvent(world, pos, () -> new EmitBlockUpdateEvent(eventType, block, methodName));
    }

    public static void onEmitBlockUpdateRedstoneDust(World world, Block block, BlockPos pos, EventType eventType, String methodName, Collection<BlockPos> updateOrder)
    {
        onEvent(world, pos, () -> new EmitBlockUpdateRedstoneDustEvent(eventType, block, methodName, pos, updateOrder));
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

    /*
     * ------------
     *  Interfaces
     * ------------
     */

    public static Optional<TickStage> getTickStage(World world)
    {
        return getWorldLogger(world).map(MicroTimingLogger::getTickStage);
    }

    public static Optional<TickStage> getTickStage()
    {
        Iterator<ServerWorld> iterator = getInstance().getLoggers().keySet().iterator();
        return iterator.hasNext() ? getTickStage(iterator.next()) : Optional.empty();
    }
}
