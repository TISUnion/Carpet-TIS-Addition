package carpettisaddition.logging.loggers.gameevent.listeners.sculk.blockreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.GameEventLogger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import net.minecraft.text.BaseText;

public class BlockReason
{
    public static final BlockReason OTHER_REASON = new BlockReason("other_reason","Other Reason");
    public static final BlockReason NOT_VIBRATION_BLOCK_REASON = new BlockReason("not_vibration","Not Vibration");
    public static final BlockReason SENSOR_PLACED_OR_REMOVED_BLOCK_REASON = new BlockReason("sensor_placed_or_removed","Sensor Placed or Removed");
    public static final BlockReason SCULK_UNAVAILABLE_BLOCK_REASON = new BlockReason("sculk_unavailable","Sculk Unavailable");

    public static final String BLOCK_REASON_TR = "blockReason.";

    private final String reasonKey;
    private final String reasonText;

    public BlockReason(String reasonKey, String reasonText) {
        this.reasonKey = reasonKey;
        this.reasonText = reasonText;
    }
    protected BaseText getFrontText(){
        return Messenger.c(
                "r " + this.tr("blocked","Blocked"),
                "r :",
                "n " + this.tr(),
                "w  "
        );
    }

    public BaseText toText(SculkSensorListenerMessenger messenger){
        return getFrontText();
    }
    protected String tr(String key,String text){
        return GameEventLogger.getStaticTranslator().tr(BLOCK_REASON_TR + key,text);
    }
    protected String tr(){
        return this.tr(reasonKey, reasonText);
    }
}
