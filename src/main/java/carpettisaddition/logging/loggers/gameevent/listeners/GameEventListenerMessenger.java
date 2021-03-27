package carpettisaddition.logging.loggers.gameevent.listeners;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.GameEventContext;
import carpettisaddition.logging.loggers.gameevent.GameEventLogger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEventListenerMessenger
{
    protected World world;
    protected GameEventListener listener;
    protected GameEventContext gameEventContext;
    protected BlockPos eventPos;
    protected final List<BaseText> msg = new ArrayList<>();
    protected int eventCount;

    public World getWorld()
    {
        return world;
    }

    public GameEventListener getListener()
    {
        return listener;
    }

    public GameEventContext getGameEventContext()
    {
        return gameEventContext;
    }

    public BlockPos getEventPos()
    {
        return eventPos;
    }

    public void addListenStartMessage()
    {
        eventCount++;
        this.msg.add(Messenger.c(
                GameEventUtil.getSomeSpace(eventCount),
                this.getListenerName(),
                "w " + " $->", // $->@ = Listener '@' started listening
                GameEventUtil.getStyledPositionText("l", getListener().getPositionSource().getPos(world), "@", gameEventContext)
        ));
    }

    public GameEventListenerMessenger(World world, BlockPos pos, GameEventListener listener, GameEventContext context)
    {
        this.world = world;
        this.eventPos = pos;
        this.listener = listener;
        this.gameEventContext = context;
        this.eventCount = 0;
        addListenStartMessage();
    }

    protected BaseText getListenerName()
    { // Styled as [ListenerName]
        BaseText msg = Messenger.s("[", "g");
        if (listener.getPositionSource().getPos(world).isPresent())
        {
            msg = Messenger.c(msg, TextUtil.getBlockName(world.getBlockState(listener.getPositionSource().getPos(world).get()).getBlock()));
        } else
        {
            msg = Messenger.c(msg, "w " + GameEventLogger.getStaticTranslator().tr("listener.no_listener_pos"));
        }
        msg = Messenger.c(msg, "g " + "]");
        return msg;
    }

    public List<BaseText> onListenEnd()
    {
        return msg;
    }
}
