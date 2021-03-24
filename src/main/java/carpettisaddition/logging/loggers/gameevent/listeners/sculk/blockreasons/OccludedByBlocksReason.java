package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import net.minecraft.text.BaseText;

public class OccludedByBlocksReason extends BlockReason
{
    public static final OccludedByBlocksReason OCCLUDED_BY_BLOCKS_REASON = new OccludedByBlocksReason();
    public static final String REASON_KEY = "occluded";
    public static final String REASON_TEXT = "Occluded";
    public OccludedByBlocksReason()
    {
        super(REASON_KEY, REASON_TEXT);
    }
    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger){
        return Messenger.c(
                this.getFrontText(),
                this.getAdditionPrompt(),
                GameEventUtil.getStyledPositionText("l",messenger.getEventPos(),"@", messenger.getGameEventContext()),
                "w  -",
                "r x",
                "w -> ",
                GameEventUtil.getStyledPositionText("r",
                        messenger.getListener().getPositionSource().getPos(messenger.getWorld()).get(),"@", messenger.getGameEventContext())
        );
    }
}
