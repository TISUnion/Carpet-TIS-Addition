package carpettisaddition.logging.loggers.microtick;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.interfaces.IWorld_MicroTickLogger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtick.enums.ActionRelation;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class MicroTickLoggerManager
{
    private static MicroTickLoggerManager instance;

    private final Map<World, MicroTickLogger> loggers = Maps.newHashMap();

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

    // called before action is done
    // [stage][detail]^[extra]

    private static Optional<MicroTickLogger> getWorldLogger(World world)
    {            
        return instance == null ? Optional.empty() : Optional.of(instance.loggers.get(world));
    }

    // called before an action is executed or done

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, ActionRelation actionType, BlockUpdateType updateType, Direction exceptSide)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onBlockUpdate(world, pos, fromBlock, actionType, updateType, updateType.getUpdateOrderList(exceptSide)));
        }
    }
    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, ActionRelation actionType, BlockUpdateType updateType)
    {
        onBlockUpdate(world, pos, fromBlock, actionType, updateType, null);
    }

    // called after an action is done

    public static void onComponentAddToTileTickList(World world, BlockPos pos, int delay, TickPriority priority)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onComponentAddToTileTickList(world, pos, delay, priority));
        }
    }
    public static void onComponentAddToTileTickList(World world, BlockPos pos, int delay)
    {
        onComponentAddToTileTickList(world, pos, delay, TickPriority.NORMAL);
    }

    public static void onPistonAddBlockEvent(World world, BlockPos pos, int eventID, int eventParam)
    {
        if (isLoggerActivated() && 0 <= eventID && eventID <= 2)
        {
            getWorldLogger(world).ifPresent(logger -> logger.onPistonAddBlockEvent(world, pos, eventID, eventParam));
        }
    }

    public static void onPistonExecuteBlockEvent(World world, BlockPos pos, Block block, int eventID, int eventParam, boolean success) // "block" only overwrites displayed name
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onPistonExecuteBlockEvent(world, pos, block, eventID, eventParam, success));
        }
    }

    public static void onComponentPowered(World world, BlockPos pos, boolean poweredState)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onComponentPowered(world, pos, poweredState));
        }
    }

    public static void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).ifPresent(logger -> logger.onRedstoneTorchLit(world, pos, litState));
        }
    }

    public static void flushMessages() // needs to call at the end of a gt
    {
        for (MicroTickLogger logger : instance.loggers.values())
        {
            logger.flushMessages();
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
        for (MicroTickLogger logger : instance.loggers.values())
        {
            logger.setTickStage(stage);
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

    public static Optional<String> getTickStage(World world)
    {
        return getWorldLogger(world).map(MicroTickLogger::getTickStage);
    }
}
