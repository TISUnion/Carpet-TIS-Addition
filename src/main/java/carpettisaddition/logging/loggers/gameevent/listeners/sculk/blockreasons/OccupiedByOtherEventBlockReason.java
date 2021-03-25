package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.world.event.GameEvent;

public class OccupiedByOtherEventBlockReason extends BlockReason
{
    private static final String REASON_KEY = "occupied";
    private static final String REASON_TEXT = "Occupied";

    private final GameEvent gameEvent;
    private final int delay;
    public OccupiedByOtherEventBlockReason(GameEvent gameEvent,int delay){
        super(REASON_KEY,REASON_TEXT);
        this.gameEvent = gameEvent;
        this.delay = delay;
    }

    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger){
        return Messenger.c(
                super.toText(messenger),
                GameEventUtil.getAdditionPrompt(),
                GameEventUtil.getStyledPositionText("l",messenger.getEventPos(),"@", messenger.getGameEventContext()),
                "w  ---> ",
                "g (",
                TextUtil.getFancyText(
                        "g",
                        Messenger.s(this.tr()),
                        Messenger.c(
                                "w " + this.tr("occupied_game_event","Occupied GameEvent"),
                                "w :" + GameEventUtil.getGameEventID(gameEvent),
                                "w \n",
                                "w " + this.tr("current_delay","Current Delay"),
                                "w :" + this.delay
                        ),
                        null),
                "g )",
                GameEventUtil.getStyledPositionText("r",
                        messenger.getListener().getPositionSource().getPos(messenger.getWorld()),"@", messenger.getGameEventContext())
        );
    }
}
