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
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GameEventLogger extends AbstractLogger
{
    public static final String NAME = "gameEvent";
    private static final GameEventLogger INSTANCE = new GameEventLogger();
    private GameEventContext gameEventContext;
    private int currentEventID;
    private long lastGameTick;
    private List<BaseText> msg = Lists.newArrayList();

    public GameEventLogger()
    {
        super(NAME);
        currentEventID = 0;
        lastGameTick = 0;
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
        return gameEvent.toString().substring(gameEvent.toString().indexOf('{') + 2, gameEvent.toString().indexOf(',') - 1).toUpperCase();
    }

    private String getColoredAttributesLabel(boolean b, String text)
    {
        return (b ? "e " : "g ") + text; // Dark green if true, gray for the else.
    }

    private BaseText getSpecialTagsForGameEvent(GameEvent gameEvent)
    {
        return Messenger.c(
                getColoredAttributesLabel(GameEventTags.IGNORE_VIBRATIONS_SNEAKING.contains(gameEvent), "Ignore Vibrations Sneaking\n"),
                getColoredAttributesLabel(GameEventTags.VIBRATIONS.contains(gameEvent), "Vibrations")
        );
    }

    private BaseText getStyledPositionText(String style, BlockPos pos, BaseText text)
    {
        return TextUtil.getFancyText(style, text,
                TextUtil.getCoordinateText("c", pos, gameEventContext.getWorldRegistryKey()),
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(pos)));
    }

    private BaseText getStyledPositionText(String style, BlockPos pos, String text)
    {
        return getStyledPositionText(style, pos, Messenger.s(text));
    }

    private void addTickStartMessage()
    {
        msg.add(Messenger.c(
                "f [",
                "f " + this.tr("GameTime"),
                "^w world.getTime()",
                "g  " + gameEventContext.getProperties().getTime(),
                "f  @ ",
                TextUtil.getFancyText(
                        "g",
                        TextUtil.getDimensionNameText(gameEventContext.getWorldRegistryKey()),
                        Messenger.s(this.gameEventContext.getWorldRegistryKey().getValue().toString()),
                        null
                ),
                "f ] ------------"
        ));
    }

    private BaseText getIDMessage()
    {
        BlockPos pos = gameEventContext.getBlockPos();
        return getStyledPositionText("f", pos, String.format("#%d", currentEventID));
    }

    public void onGameEventStartProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range, RegistryKey<World> registryKey, WorldProperties properties)
    {
        gameEventContext = new GameEventContext(gameEvent, pos, null, range, entity, GameEventStatus.NOTHING, registryKey, properties);
        msg.clear();
        if (this.gameEventContext.getProperties().getTime() != lastGameTick)
        { // only flush with overworld time changed
            lastGameTick = this.gameEventContext.getProperties().getTime();
            currentEventID = 0;
            addTickStartMessage();
        }
        msg.add(Messenger.c(
                getIDMessage(),
                TextUtil.getSpaceText(),
                Messenger.s("[", "e"),
                TextUtil.getFancyText("e", Messenger.s(getGameEventID(gameEvent)), getSpecialTagsForGameEvent(gameEvent), null),
                Messenger.s("]", "e"),
                TextUtil.getSpaceText()
        ));
    }

    public void onGameEventEndProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, int range)
    {
        msg.add(Messenger.c(
                getIDMessage(),
                TextUtil.getSpaceText(),
                "r [ENDED]"
        ));
        currentEventID++;
        flushMessages();
    }

    public void onGameEventListen(World world, GameEventListener listener)
    {
        gameEventContext.setStatus(GameEventStatus.CAUGHT);
        listener.getPositionSource().getPos(world).ifPresent((pos) ->
        { // No isPresentOrElse in Java 8.
            msg.add(Messenger.c(
                    TextUtil.getSpaceText(),
                    getIDMessage(),
                    TextUtil.getSpaceText(),
                    "t [" + this.tr("listener", "Listener") + "] ",
                    Messenger.s(this.tr("caught", "caught.")),
                    TextUtil.getSpaceText(),
                    getStyledPositionText("l", pos, "@")));
        });
        // deprecated, in fact idk how to reach this code at all.
        /*
        if (!listener.getPositionSource().getPos(world).isPresent())
        { // stupidly implements,
            msg.add(Messenger.c(
                    TextUtil.getSpaceText(),
                    Messenger.s(this.tr("this_gameevent_was_caught_with_unknown_listener_position", "This Game Event was caught with unknown position"), "w")
            ));
        }
         */
    }
    public void onGameEventSculkSensed(World world, GameEvent gameEvent, BlockPos blockPos, BlockPos sourcePos)
    {
        gameEventContext.setStatus(GameEventStatus.SCULK_SENSED);
        msg.add(Messenger.c(
                "w " + "  ",
                getIDMessage(),
                TextUtil.getSpaceText(),
                "g [",
                TextUtil.getBlockName(world.getBlockState(sourcePos).getBlock()),
                "g ] ",
                getStyledPositionText("l", blockPos, "@"),
                "w " + " ---> ",
                getStyledPositionText("l", sourcePos, "@")
        ));
    }

    // Failure path pattern: source -x-> listener
    // hover text on 'x' with click TP event.
    // to be finished.
    public void onGameEventOccluded(BlockPos blockPos, BlockPos sourcePos, BlockHitResult hitResult)
    {
        msg.add(Messenger.c(
                "w " + "  ",
                getIDMessage(),
                TextUtil.getSpaceText(),
                "w [" + this.tr("listener", "Listener") + "] ",
                getStyledPositionText("l", blockPos, "@"),
                "w " + " -",
                "r " + "x",
                "w " + "-> ",
                getStyledPositionText("l", sourcePos, "@")
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
