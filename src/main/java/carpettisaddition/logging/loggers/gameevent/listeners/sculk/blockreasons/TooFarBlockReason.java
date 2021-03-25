package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil;
import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class TooFarBlockReason extends BlockReason
{
    private static final String REASON_KEY = "too_far";
    private static final String REASON_TEXT = "Too Far";

    private final double distance;
    private final int range;
    public TooFarBlockReason(double d,int r)
    {
        super(REASON_KEY, REASON_TEXT);
        this.distance = d;
        this.range = r;
    }

    @Override
    public BaseText toText(SculkSensorListenerMessenger messenger)
    {
        Optional<BlockPos> pos = messenger.getListener().getPositionSource().getPos(messenger.getWorld());
        return Messenger.c(
                super.toText(messenger),
                GameEventUtil.getAdditionPrompt(),
                GameEventUtil.getStyledPositionText("l", messenger.getEventPos(), "@", messenger.getGameEventContext()),
                "w  -",
                TextUtil.getFancyText(
                        "g",
                        Messenger.s("--"),
                        Messenger.c(
                                "w " + this.tr("distance","Distance"),
                                "w :",
                                "w " + MathHelper.sqrt(distance),
                                "w \n",
                                "w " + this.tr("listener_range","Listener Range"),
                                "w :",
                                "w " + MathHelper.sqrt(range)
                                )
                        ,null),
                "k - - -> ",
                GameEventUtil.getStyledPositionText("r",
                        pos, "@", messenger.getGameEventContext())

        );
    }
}
