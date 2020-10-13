package carpettisaddition.logging.loggers.microtick;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.interfaces.IWorld_MicroTickLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.enums.MessageType;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.Block;
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
    }

    public static void detachServer()
    {
        instance = null;
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

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Direction exceptSide, MessageType messageType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onBlockUpdate(world, pos, fromBlock, updateType, updateType.getUpdateOrderList(exceptSide), messageType));
        }
    }

    /*
     * -----------
     *  Tile Tick
     * -----------
     */

    public static void onExecuteTileTickEvent(World world, ScheduledTick<Block> event, MessageType messageType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onExecuteTileTickEvent(world, event, messageType));
        }
    }

    public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onScheduleTileTickEvent(world, block, pos, delay, priority));
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

    public static void onExecuteBlockEvent(World world, BlockAction blockAction, Boolean returnValue, MessageType messageType)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onExecuteBlockEvent(world, blockAction, returnValue, messageType));
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

    public static void onComponentPowered(World world, BlockPos pos, boolean poweredState)  // TODO: do more injection
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onComponentPowered(world, pos, poweredState));
        }
    }

    public static void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)  // TODO: do more injection
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onRedstoneTorchLit(world, pos, litState));
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

    public static void setTickStageExtra(World world, TickStage stage)
    {
        getWorldLogger(world).ifPresent(logger -> logger.setTickStageExtra(stage));
    }
    public static void setTickStageExtra(TickStage stage)
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
