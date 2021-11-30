package carpettisaddition.logging.loggers.microtiming.utils;

import carpet.logging.Logger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MicroTimingStandardCarpetLogger extends Logger
{
	private static final MicroTimingStandardCarpetLogger INSTANCE = createInstance();

	public static final String NAME = MicroTimingLogger.NAME;
	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("carpet_logger");

	private MicroTimingStandardCarpetLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(acceleratorField, logName, def, options, true);
	}

	private static MicroTimingStandardCarpetLogger createInstance()
	{
		String def = MicroTimingLogger.LoggingOption.DEFAULT.toString();
		String[] options = Arrays.stream(MicroTimingLogger.LoggingOption.values()).map(MicroTimingLogger.LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
		return new MicroTimingStandardCarpetLogger(TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, def, options);
	}

	public static MicroTimingStandardCarpetLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void addPlayer(String playerName, String option)
	{
		super.addPlayer(playerName, option);
		ServerPlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			if (!MicroTimingLoggerManager.isLoggerActivated())
			{
				String command = "/carpet microTiming true";
				Messenger.tell(
						player,
						Messenger.fancy(
								translator.tr("rule_hint", command),
								translator.tr("click_to_execute"),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
						)
				);
			}
			MicroTimingMarkerManager.getInstance().sendAllMarkersForPlayer(player);
		}
	}

	@Override
	public void removePlayer(String playerName)
	{
		super.removePlayer(playerName);
		ServerPlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			MicroTimingMarkerManager.getInstance().cleanAllMarkersForPlayer(player);
		}
	}

	public void onCarpetClientHello(ServerPlayerEntity player)
	{
		if (MicroTimingUtil.isPlayerSubscribed(player))
		{
			MicroTimingMarkerManager.getInstance().sendAllMarkersForPlayer(player);
		}
	}
}
