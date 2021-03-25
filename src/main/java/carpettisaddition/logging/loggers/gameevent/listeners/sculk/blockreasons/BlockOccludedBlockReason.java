package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import net.minecraft.text.BaseText;

public class BlockOccludedBlockReason extends BlockReason
{
    public static final BlockOccludedBlockReason OCCLUDED_BY_BLOCKS_REASON = new BlockOccludedBlockReason();
    public static final String REASON_KEY = "occluded";
    public static final String REASON_TEXT = "Occluded";
    public BlockOccludedBlockReason()
    {
        super(REASON_KEY, REASON_TEXT);
    }
    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger){
        return Messenger.c(
                super.toText(messenger),
                GameEventUtil.getAdditionPrompt(),
                GameEventUtil.getStyledPositionText("l",messenger.getEventPos(),"@", messenger.getGameEventContext()),
                "w  -",
                "r x",
                "w -> ",
                GameEventUtil.getStyledPositionText("r",
                        messenger.getListener().getPositionSource().getPos(messenger.getWorld()),"@", messenger.getGameEventContext())
        );
    }
}
