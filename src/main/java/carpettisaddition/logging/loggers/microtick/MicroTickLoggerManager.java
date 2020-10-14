package carpettisaddition.logging.loggers.microtick;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.interfaces.IWorld_MicroTickLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.types.PowerState;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockAction;
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

    private final Map<World, MicroTickLogger> loggers = new Reference2ObjectArrayMap<>();

    public MicroTickLoggerManager(MinecraftServer minecraftServer)
    {
        for (World world : minecraftServer.getWorlds())
        {
            this.loggers.put(world, ((IWorld_MicroTickLogger)world).getMicroTickLogger());
        }
    }

    public static boolean isLoggerActivated()
    {
        return CarpetTISAdditionSettings.microTick && ExtensionLoggerRegistry.__microtick && instance != null;
    }

    public static void attachServer(MinecraftServer minecraftServer)
    {
        instance = new MicroTickLoggerManager(minecraftServer);
        CarpetTISAdditionServer.LOGGER.info("Attached MicroTick loggers to " + instance.loggers.size() + " worlds");
    }

    public static void detachServer()
    {
        instance = null;
        CarpetTISAdditionServer.LOGGER.info("Detached MicroTick loggers");
    }

    private static Optional<MicroTickLogger> getWorldLogger(World world)
    {
        return instance == null ? Optional.empty() : Optional.ofNullable(instance.loggers.get(world));
    }

    /*
     * --------------
     *  Block Update
     * --------------
     */

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onBlockUpdate(world, pos, fromBlock, updateType, updateType.getUpdateOrderList(exceptSide), eventType));
        }
    }

    public static void onSetBlockState(World world, BlockPos pos, BlockState state, Boolean returnValue, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onSetBlockState(world, pos, state, returnValue, eventType));
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

    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onScheduleTileTick(world, block, pos, delay, priority));
        }
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

    public static void onExecuteBlockEvent(World world, BlockAction blockAction, Boolean returnValue, EventType eventType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onExecuteBlockEvent(world, blockAction, returnValue, eventType));
        }
    }

    public static void onScheduleBlockEvent(World world, BlockAction blockAction)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onScheduleBlockEvent(world, blockAction));
        }
    }


    /*
     * ------------------------
     *  Component state change
     * ------------------------
     */

    public static void onComponentPowered(World world, BlockPos pos, PowerState poweredState)  // TODO: do more injection
    {
        if (isLoggerActivated())
        {
        }
    }
    public static void onComponentPowered(World world, BlockPos pos, boolean powered)
    {
        onComponentPowered(world, pos, PowerState.of(powered));
    }
    public static void onComponentPowered(World world, BlockPos pos, int signalStrength)
    {
        onComponentPowered(world, pos, PowerState.of(signalStrength));
    }

    public static void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)  // TODO: do more injection
    {
        if (isLoggerActivated())
        {
        }
    }

    public static void flushMessages() // needs to call at the end of a gt
    {
        if (instance != null)
        {
            for (MicroTickLogger logger : instance.loggers.values())
            {
                logger.flushMessages();
            }
        }
    }

    public static void setTickStage(World world, String stage)
    {
        getWorldLogger(world).ifPresent(logger -> {
            logger.setTickStage(stage);
            logger.setTickStageDetail(null);
            logger.setTickStageExtra(null);
        });
    }
    public static void setTickStage(String stage)
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

    public static void setTickStageExtra(World world, ToTextAble stage)
    {
        getWorldLogger(world).ifPresent(logger -> logger.setTickStageExtra(stage));
    }
    public static void setTickStageExtra(ToTextAble stage)
    {
        if (instance != null)
        {
            for (MicroTickLogger logger : instance.loggers.values())
            {
                logger.setTickStageExtra(stage);
            }
        }
    }

    public static Optional<String> getTickStage(World world)
    {
        return getWorldLogger(world).map(MicroTickLogger::getTickStage);
    }
}
