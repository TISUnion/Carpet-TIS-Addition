package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.text.MutableText;

public enum PistonBlockEventType
{
	PUSH,
	RETRACT,
	DROP,
	UNKNOWN;

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("piston_block_event_type");
	private static final PistonBlockEventType[] VALUES = PistonBlockEventType.values();
	private final String translationKey;

	PistonBlockEventType()
	{
		this.translationKey = this.name().toLowerCase();
	}

	public MutableText toText()
	{
		return translator.tr(this.translationKey);
	}

	public static PistonBlockEventType fromId(int id)
	{
		return 0 <= id && id <= 2 ? VALUES[id] : UNKNOWN;
	}
}
