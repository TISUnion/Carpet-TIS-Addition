package carpettisaddition.logging.loggers.gameevent.utils;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.gameevent.GameEventContext;
import carpettisaddition.utils.TextUtil;
import net.minecraft.tag.GameEventTags;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class GameEventUtil
{
    public static String getColoredAttributesLabel(boolean b, String text)
    {
        return (b ? "e " : "g ") + text; // Dark green if true, gray for the else.
    }

    public static String getGameEventID(GameEvent gameEvent)
    {
        return gameEvent.toString().substring(gameEvent.toString().indexOf('{') + 2, gameEvent.toString().indexOf(',') - 1).toUpperCase();
    }
    public static BaseText getSomeSpace(int number){
        return Messenger.c("w " + String.format("%1$" + number + "s",""));
    }

    public static BaseText getSpecialTagsForGameEvent(GameEvent gameEvent)
    {
        return Messenger.c(
                getColoredAttributesLabel(GameEventTags.IGNORE_VIBRATIONS_SNEAKING.contains(gameEvent), "Ignore Vibrations Sneaking\n"),
                getColoredAttributesLabel(GameEventTags.VIBRATIONS.contains(gameEvent), "Vibrations")
        );
    }

    public static BaseText getStyledPositionText(String style, BlockPos pos, BaseText text, GameEventContext gameEventContext)
    {
        return TextUtil.getFancyText(style, text,
                TextUtil.getCoordinateText("c", pos, gameEventContext.getWorldRegistryKey()),
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(pos)));
    }

    public static BaseText getStyledPositionText(String style, BlockPos pos, String text, GameEventContext gameEventContext)
    {
        return getStyledPositionText(style, pos, Messenger.s(text), gameEventContext);
    }
}
