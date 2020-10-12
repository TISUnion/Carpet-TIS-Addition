package carpettisaddition.logging.loggers.microtick;

import carpet.CarpetServer;
import carpettisaddition.CarpetTISAdditionSettings;
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

public class MicroTickLoggerManager
{
    public static MicroTickLoggerManager instance = new MicroTickLoggerManager(CarpetServer.minecraft_server);
    private final Map<World, MicroTickLogger> loggers = Maps.newHashMap();

    public MicroTickLoggerManager(MinecraftServer mcServer)
    {
        for (World world : mcServer.getWorlds())
        {
            this.loggers.put(world, new MicroTickLogger(world));
        }
    }

    public static boolean isLoggerActivated()
    {
        return CarpetTISAdditionSettings.microTick  && ExtensionLoggerRegistry.__microtick;
    }

    // called before action is done
    // [stage][detail]^[extra]

    public static MicroTickLogger getWorldLogger(World world)
    {
        return instance.loggers.get(world);
    }

    // called before an action is executed or done

    public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, ActionRelation actionType, BlockUpdateType updateType, Direction exceptSide)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).onBlockUpdate(world, pos, fromBlock, actionType, updateType, updateType.getUpdateOrderList(exceptSide));
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
            getWorldLogger(world).onComponentAddToTileTickList(world, pos, delay, priority);
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
            getWorldLogger(world).onPistonAddBlockEvent(world, pos, eventID, eventParam);
        }
    }

    public static void onPistonExecuteBlockEvent(World world, BlockPos pos, Block block, int eventID, int eventParam, boolean success) // "block" only overwrites displayed name
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).onPistonExecuteBlockEvent(world, pos, block, eventID, eventParam, success);
        }
    }

    public static void onComponentPowered(World world, BlockPos pos, boolean poweredState)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).onComponentPowered(world, pos, poweredState);
        }
    }

    public static void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)
    {
        if (isLoggerActivated())
        {
            getWorldLogger(world).onRedstoneTorchLit(world, pos, litState);
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
        MicroTickLogger logger = getWorldLogger(world);
        logger.setTickStage(stage);
        logger.setTickStageDetail(null);
        logger.setTickStageExtra(null);
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
        getWorldLogger(world).setTickStageDetail(detail);
    }

    public static void setTickStageExtra(World world, TickStage stage)
    {
        getWorldLogger(world).setTickStageExtra(stage);
    }

    public static String getTickStage(World world)
    {
        return getWorldLogger(world).getTickStage();
    }
}
