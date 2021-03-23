package carpettisaddition.logging.loggers.gameevent.listeners;

import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEventListenerMessenger
{
    protected GameEventListener listener;
    protected World world;
    protected BlockPos eventPos;
    protected final List<BaseText> msg = new ArrayList<BaseText>();

    public GameEventListenerMessenger(GameEventListener listener)
    {
        this.listener = listener;
    }

    public List<BaseText> getMessage()
    {
        return msg;
    }

    abstract void onGameEventListenerFinished();
}
