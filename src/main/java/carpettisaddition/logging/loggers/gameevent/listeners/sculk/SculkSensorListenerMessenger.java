package carpettisaddition.logging.loggers.gameevent.listeners.sculk;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.GameEventContext;
import carpettisaddition.logging.loggers.gameevent.listeners.GameEventListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons.BlockReason;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

public class SculkSensorListenerMessenger extends GameEventListenerMessenger
{

    public SculkSensorListenerMessenger(World world, BlockPos pos, GameEventListener listener, GameEventContext context)
    {
        super(world, pos, listener,context);
    }
    public void onSculkDecideToActivate(){
        eventCount++;
        this.msg.add(Messenger.c(
                GameEventUtil.getSomeSpace(eventCount),
                this.getListenerName(),
                TextUtil.getSpaceText(),
                GameEventUtil.getStyledPositionText("l",eventPos,"@", gameEventContext),
                "w  ---> ",
                GameEventUtil.getStyledPositionText("l",
                       listener.getPositionSource().getPos(world).get(),"@", gameEventContext)
        ));
    }
    public void onSculkBlocked(BlockReason reason){
        eventCount++;
        this.msg.add(Messenger.c(
                GameEventUtil.getSomeSpace(eventCount),
                this.getListenerName(),
                reason.toText(this)
        ));
    }
}
