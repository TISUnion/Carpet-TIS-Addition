package carpettisaddition.logging.loggers.microtiming.utils;

import carpet.logging.Logger;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MicroTimingStandardCarpetLogger extends Logger
{
	public static final String NAME = MicroTimingLogger.NAME;
	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("carpet_logger");

	private MicroTimingStandardCarpetLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(acceleratorField, logName, def, options);
	}

	public static MicroTimingStandardCarpetLogger create()
	{
		String def = MicroTimingLogger.LoggingOption.DEFAULT.toString();
		String[] options = Arrays.stream(MicroTimingLogger.LoggingOption.values()).map(MicroTimingLogger.LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
		return new MicroTimingStandardCarpetLogger(TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, def, options);
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
				Messenger.m(player, Messenger.c(
						"w " + String.format(translator.tr("rule_hint", "Use command %s to start logging"), command),
						"?" + command,
						"^w " + translator.tr("Click to execute")
				));
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

	@Override
	public void onPlayerConnect(PlayerEntity player, boolean firstTime)
	{
		super.onPlayerConnect(player, firstTime);
		if (player instanceof ServerPlayerEntity && MicroTimingUtil.isPlayerSubscribed(player))
		{
			MicroTimingMarkerManager.getInstance().sendAllMarkersForPlayer((ServerPlayerEntity)player);
		}
	}
}
