package carpettisaddition.logging.loggers.gameevent;

import carpettisaddition.logging.loggers.gameevent.enums.GameEventStatus;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.event.GameEvent;

public class GameEventContext
{
    private GameEvent gameEvent;
    private BlockPos blockPos;
    private BlockPos sensorPos;
    private int range;
    private Entity entity;
    private GameEventStatus status;
    private RegistryKey<World> worldRegistryKey;
    private WorldProperties properties;

    public GameEventContext(GameEvent gameEvent, BlockPos blockPos, BlockPos sensorPos, int range, Entity entity, GameEventStatus result, RegistryKey<World> worldRegistryKey, WorldProperties properties)
    {
        this.gameEvent = gameEvent;
        this.blockPos = blockPos;
        this.sensorPos = sensorPos;
        this.range = range;
        this.entity = entity;
        this.status = result;
        this.worldRegistryKey = worldRegistryKey;
        this.properties = properties;
    }

    public GameEvent getGameEvent()
    {
        return gameEvent;
    }

    public RegistryKey<World> getWorldRegistryKey()
    {
        return worldRegistryKey;
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

    public WorldProperties getProperties()
    {
        return properties;
    }
}
