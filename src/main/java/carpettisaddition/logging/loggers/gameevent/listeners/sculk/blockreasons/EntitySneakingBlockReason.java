package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

public class EntitySneakingBlockReason extends BlockReason
{
    private static final String REASON_KEY = "entity_sneaking";
    private static final String REASON_TEXT = "Entity Sneaking";
    private final Entity entity;
    public EntitySneakingBlockReason(Entity entity){
        super(REASON_KEY,REASON_TEXT);
        this.entity = entity;
    }

    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger)
    {
        return Messenger.c(
                super.toText(messenger),
                GameEventUtil.getAdditionPrompt(),
                TextUtil.getEntityText("c",entity)
        );
    }
}
