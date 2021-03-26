package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

public class ItemOccludedBlockReason extends BlockReason
{
    private static final String REASON_KEY = "item_occluded";
    private static final String REASON_TEXT = "Item Occluded";
    private final Entity item;

    public ItemOccludedBlockReason(Entity item){
        super(REASON_KEY,REASON_TEXT);
        this.item = item;
    }

    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger)
    {
        return Messenger.c(
                super.toText(messenger),
                GameEventUtil.getAdditionPrompt(),
                TextUtil.getEntityText("c",item)
        );
    }
}
