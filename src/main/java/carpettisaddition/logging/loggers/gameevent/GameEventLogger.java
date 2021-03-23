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

import static carpettisaddition.logging.loggers.gameevent.utils.GameEventUtils.*;

public class GameEventLogger extends AbstractLogger
{
    public static final String NAME = "gameEvent";
    private static final GameEventLogger INSTANCE = new GameEventLogger();
    private GameEventContext gameEventContext;
    private GameEventListener listener;
    private int currentEventID;
    private long lastGameTick;
    private final List<BaseText> msg = Lists.newArrayList();

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
        return getStyledPositionText("f", pos, String.format("#%d", currentEventID), gameEventContext);
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
                    getStyledPositionText("l", pos, "@", gameEventContext)));
        });
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
                getStyledPositionText("l", blockPos, "@", gameEventContext),
                "w " + " ---> ",
                getStyledPositionText("l", sourcePos, "@", gameEventContext)
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
                getStyledPositionText("l", blockPos, "@", gameEventContext),
                "w " + " -",
                "r " + "x",
                "w " + "-> ",
                getStyledPositionText("l", sourcePos, "@", gameEventContext)
        ));
    }

    private enum LoggingOption
    {
        ALL,
        VIBRATION,
        IN_RANGE,
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
