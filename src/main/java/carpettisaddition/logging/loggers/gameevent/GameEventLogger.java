package carpettisaddition.logging.loggers.gameevent;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.gameevent.enums.GameEventStatus;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.tag.GameEventTags;
import net.minecraft.text.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameEventLogger extends AbstractLogger
{
    public static final String NAME = "gameEvent";
    private static final GameEventLogger INSTANCE = new GameEventLogger();
    private GameEventContext gameEventContext;
    private List<BaseText> msg = Lists.newArrayList();

    private GameEventLogger()
    {
        super(NAME);
    }

    public boolean isLoggerActivated()
    {
        return TISAdditionLoggerRegistry.__gameEvent;
    }

    public static GameEventLogger getInstance()
    {
        return INSTANCE;
    }

    public Logger getStandardLogger()
    {
        String def = LoggingOption.DEFAULT.toString().toLowerCase();
        String[] option = Arrays.stream(LoggingOption.values()).map(LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
        return TISAdditionLoggerRegistry.standardLogger(NAME, def, option);
    }

    public GameEventLogger(String name)
    {
        super(name);
    }


    private BaseText[] getMessageByOption(LoggingOption option)
    {
        BaseText[] res = msg.toArray(new BaseText[0]);
        switch (option)
        {
            case ALL:
                return res;
            case VIBRATION:
                if (GameEventTags.VIBRATIONS.contains(gameEventContext.getGameEvent()))
                {
                    return res;
                }
                break;
            case SCULK_SENSED:
                if (gameEventContext.getStatus() == GameEventStatus.SCULK_SENSED)
                {
                    return res;
                }
                break;
        }
        return new BaseText[0];
    }

    private void flushMessages()
    {
        LoggerRegistry.getLogger(NAME).log((option) -> getMessageByOption(LoggingOption.getOrDefault(option)));
    }

    private String getGameEventID(GameEvent gameEvent)
    {
        return gameEvent.toString().substring(gameEvent.toString().indexOf('{') + 1, gameEvent.toString().indexOf(',')).toUpperCase();
    }

    public void onGameEventStartProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range)
    {
        gameEventContext = new GameEventContext(gameEvent, pos, null, range, entity, GameEventStatus.NOTHING);
        msg.clear();
        msg.add(Messenger.c(
                "w " + this.tr("detected", "Detected"),
                TextUtil.getSpaceText(),
                "e " + this.tr("game_event", "Game Event"),
                TextUtil.getSpaceText(),
                TextUtil.getFancyText("pb", Messenger.s(getGameEventID(gameEvent)),
                        GameEventTags.VIBRATIONS.contains(gameEvent) ?
                                Messenger.c("l " + this.tr("vibration", "vibration")) :
                                Messenger.c("r " + this.tr("simple", "simple")), null),
                TextUtil.getSpaceText(),
                TextUtil.getFancyText("qu", Messenger.s(this.tr("here", "here"))
                        , Messenger.tp("c", pos), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(pos)))
        ));
    }

    public void onGameEventEndProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range)
    {
        flushMessages();
    }

    public void onGameEventListen(World world, GameEventListener listener)
    {
        gameEventContext.setStatus(GameEventStatus.CAUGHT);
        listener.getPositionSource().getPos(world).ifPresent((pos) ->
        { // No isPresentOrElse in Java 8.
            msg.add(Messenger.c(
                    TextUtil.getSpaceText(),
                    Messenger.s(this.tr("this_gameevent_was_caught", "This Game Event was caught")),
                    TextUtil.getSpaceText(),
                    TextUtil.getFancyText("qu", Messenger.s(this.tr("here", "here"))
                            , Messenger.tp("c", pos), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(pos)))));
        });
        if (!listener.getPositionSource().getPos(world).isPresent())
        { // stupidly implements
            msg.add(Messenger.c(
                    TextUtil.getSpaceText(),
                    Messenger.s(this.tr("this_gameevent_was_caught_with_unknown_listener_position", "This Game Event was caught with unknown position"), "w")
            ));
        }
    }

    public void onGameEventSculkSensed(World world, GameEvent gameEvent, BlockPos blockPos, BlockPos sourcePos)
    {
        gameEventContext.setStatus(GameEventStatus.SCULK_SENSED);
        msg.add(Messenger.c(
                "r " + this.tr("wow", "  Wow!"),
                TextUtil.getSpaceText(),
                TextUtil.getFancyText("tb", Messenger.s(this.tr("sculk_sensor", "Sculk Sensor")),
                        Messenger.tp("c", sourcePos),
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(sourcePos))),
                TextUtil.getSpaceText(),
                "w " + this.tr("has_detected", "has detected this event.")
        ));
    }

    public void onGameEventOccluded(HitResult hitResult)
    {
        msg.add(Messenger.c(
                "r " + this.tr("oops", "  Oops!"),
                TextUtil.getSpaceText(),
                "w " + this.tr("path_blocked", "Event path is blocked")
        ));
    }

    private enum LoggingOption
    {
        ALL,
        VIBRATION,
        SCULK_SENSED;

        public static final LoggingOption DEFAULT = ALL;

        public static LoggingOption getOrDefault(String option)
        {
            LoggingOption loggingOption;
            try
            {
                loggingOption = LoggingOption.valueOf(option.toUpperCase());
            } catch (IllegalArgumentException e)
            {
                loggingOption = LoggingOption.DEFAULT;
            }
            return loggingOption;
        }
    }
}
