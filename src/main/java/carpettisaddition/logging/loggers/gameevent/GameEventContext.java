package carpettisaddition.logging.loggers.gameevent;

import carpettisaddition.logging.loggers.gameevent.enums.GameEventStatus;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.event.GameEvent;

public class GameEventContext
{
    private final GameEvent gameEvent;
    private BlockPos blockPos;
    private final Entity entity;
    private GameEventStatus status;
    private final RegistryKey<World> worldRegistryKey;
    private final WorldProperties properties;

    public GameEventContext(GameEvent gameEvent, BlockPos blockPos, Entity entity, GameEventStatus result, RegistryKey<World> worldRegistryKey, WorldProperties properties)
    {
        this.gameEvent = gameEvent;
        this.blockPos = blockPos;
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
