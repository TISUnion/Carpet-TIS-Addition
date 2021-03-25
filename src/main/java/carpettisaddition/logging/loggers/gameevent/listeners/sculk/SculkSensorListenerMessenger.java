package carpettisaddition.logging.loggers.gameevent.listeners.sculk;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.GameEventContext;
import carpettisaddition.logging.loggers.gameevent.listeners.GameEventListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons.BlockReason;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.List;

public class SculkSensorListenerMessenger extends GameEventListenerMessenger
{
    private boolean reasonable;

    public SculkSensorListenerMessenger(World world, BlockPos pos, GameEventListener listener, GameEventContext context)
    {
        super(world, pos, listener, context);
        reasonable = false;
    }

    private void addEventMessage(BaseText text)
    {
        eventCount++;
        this.reasonable = true;
        this.msg.add(Messenger.c(
                GameEventUtil.getSomeSpace(eventCount),
                this.getListenerName(),
                TextUtil.getSpaceText(),
                text
        ));
    }

    public void onSculkDecideToActivate()
    {
        addEventMessage(Messenger.c(
                GameEventUtil.getStyledPositionText("l", eventPos, "@", gameEventContext),
                "w  ---> ",
                GameEventUtil.getStyledPositionText("l",
                        listener.getPositionSource().getPos(world), "@", gameEventContext)
        ));
    }

    public void onSculkBlocked(BlockReason reason)
    {
        addEventMessage(Messenger.c(
                reason.toText(this)
        ));
    }

    @Override
    public List<BaseText> onListenEnd()
    {
        if (!this.reasonable)
        {
            //onSculkBlocked(BlockReason.OTHER_REASON);
        }
        return this.msg;
    }
}
