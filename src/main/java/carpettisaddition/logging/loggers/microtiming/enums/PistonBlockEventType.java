package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.text.BaseText;

public enum PistonBlockEventType
{
	PUSH("Push"),
	RETRACT("Retract"),
	DROP("Drop");

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("piston_block_event_type");
	private static final PistonBlockEventType[] VALUES = PistonBlockEventType.values();
	private final String translationKey;

	PistonBlockEventType(String name)
	{
		this.translationKey = name.toLowerCase();
	}

	public BaseText toText()
	{
		return translator.tr(this.translationKey);
	}

	public static PistonBlockEventType byId(int id)
	{
		return VALUES[Math.abs(id % VALUES.length)];
	}
}
