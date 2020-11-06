package carpettisaddition.logging.loggers.microtiming.utils;

import carpet.logging.Logger;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.entity.player.PlayerEntity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MicroTimingStandardCarpetLogger extends Logger
{
	public static final String NAME = MicroTimingLogger.NAME;
	private static final Translator translator = new Translator("logger", NAME + ".carpet_logger");

	private MicroTimingStandardCarpetLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(acceleratorField, logName, def, options);
	}

	public static MicroTimingStandardCarpetLogger create()
	{
		try
		{
			String def = MicroTimingLogger.LoggingOption.DEFAULT.toString();
			String[] options = Arrays.stream(MicroTimingLogger.LoggingOption.values()).map(MicroTimingLogger.LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
			return new MicroTimingStandardCarpetLogger(ExtensionLoggerRegistry.class.getField("__" + NAME), NAME, def, options);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException("Failed to create " + MicroTimingStandardCarpetLogger.class.getName());
		}
	}

	@Override
	public void addPlayer(String playerName, String option)
	{
		super.addPlayer(playerName, option);
		PlayerEntity player = this.playerFromName(playerName);
		if (player != null && !MicroTimingLoggerManager.isLoggerActivated())
		{
			String command = "/carpet microTiming true";
			Messenger.m(player, Messenger.c(
					"w " + String.format(translator.tr("rule_hint", "Use command %s to start logging"), command),
					"?" + command,
					"^w " + translator.tr("Click to execute")
			));
		}
	}
}
