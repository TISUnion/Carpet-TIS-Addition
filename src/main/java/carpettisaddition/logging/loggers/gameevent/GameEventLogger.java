package carpettisaddition.logging.loggers.gameevent;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.gameevent.enums.GameEventStatus;
import carpettisaddition.logging.loggers.gameevent.listeners.GameEventListenerMessenger;
import carpettisaddition.logging.loggers.gameevent.listeners.sculk.SculkSensorListenerMessenger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.tag.GameEventTags;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.SculkSensorListener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static carpettisaddition.logging.loggers.gameevent.utils.GameEventUtil.*;

public class GameEventLogger extends AbstractLogger
{
    public static final String NAME = "gameEvent";
    private static final GameEventLogger INSTANCE = new GameEventLogger();
    private GameEventContext gameEventContext;
    private GameEventListenerMessenger messenger;
    private int currentEventID;
    private long lastGameTick;
    private static final Translator TRANSLATOR = (new GameEventLogger()).getTranslator();
    private final List<BaseText> msg = Lists.newArrayList();

    public GameEventLogger()
    {
        super(NAME);
        currentEventID = 0;
        lastGameTick = 0;
    }



    public static Translator getStaticTranslator(){
        return TRANSLATOR;
    }
    public static GameEventLogger getInstance()
    {
        return INSTANCE;
    }

    public boolean isLoggerActivated()
    {
        return TISAdditionLoggerRegistry.__gameEvent;
    }

    public Logger getStandardLogger()
    {
        String def = LoggingOption.DEFAULT.toString().toLowerCase();
        String[] option = Arrays.stream(LoggingOption.values()).map(LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
        return TISAdditionLoggerRegistry.standardLogger(NAME, def, option);
    }

    public GameEventListenerMessenger getMessenger()
    {
        return messenger;
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
            case LISTENER_CAUGHT:
                if(gameEventContext.getStatus() == GameEventStatus.LISTENER_CAUGHT)
                {
                    return res;
                }
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

    public void onGameEventStartProcessing(@Nullable Entity entity, GameEvent gameEvent, BlockPos pos, RegistryKey<World> registryKey, WorldProperties properties)
    {
        gameEventContext = new GameEventContext(gameEvent, pos, entity, GameEventStatus.NOTHING, registryKey, properties);
        msg.clear();
        if (this.gameEventContext.getProperties().getTime() != lastGameTick)
        {
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
                "r [END]"
        ));
        currentEventID++;
        flushMessages();
    }

    public void onGameEventListenStart(World world, GameEventListener listener)
    {
        gameEventContext.setStatus(GameEventStatus.LISTENER_CAUGHT);
        if(listener instanceof SculkSensorListener){
            messenger = new SculkSensorListenerMessenger(world,gameEventContext.getBlockPos(),listener,gameEventContext);
        }
    }
    public void onGameEventListenEnd(){
        msg.addAll(messenger.onListenEnd());
    }



    private enum LoggingOption
    {
        ALL,
        VIBRATION, // Currently(21w10a) all gameEvent has this tag, so this option is equivalent to 'all'.
        LISTENER_CAUGHT;

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
