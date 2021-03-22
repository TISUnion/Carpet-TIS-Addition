package carpettisaddition.logging.loggers.gameevent;

import carpettisaddition.logging.loggers.gameevent.enums.GameEventStatus;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class GameEventContext
{
    private GameEvent gameEvent;
    private BlockPos blockPos;
    private BlockPos sensorPos;
    private int range;
    private Entity entity;
    private GameEventStatus status;

    public GameEventContext(GameEvent gameEvent, BlockPos blockPos, BlockPos sensorPos, int range, Entity entity, GameEventStatus result)
    {
        this.gameEvent = gameEvent;
        this.blockPos = blockPos;
        this.sensorPos = sensorPos;
        this.range = range;
        this.entity = entity;
        this.status = result;
    }

    public GameEvent getGameEvent()
    {
        return gameEvent;
    }


    public BlockPos getBlockPos()
    {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }

    public GameEventStatus getStatus()
    {
        return status;
    }

    public void setStatus(GameEventStatus status)
    {
        this.status = status;
    }
}
