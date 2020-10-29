package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class StringTickStageExtra extends TickStageExtraBase
{
	public static final StringTickStageExtra ENTITY_WEATHER_EFFECT = new StringTickStageExtra("Ticking weather effect entities", "ticking_weather_effect_entities");
	public static final StringTickStageExtra ENTITY_REGULAR = new StringTickStageExtra("Ticking regular entities", "ticking_regular_entities");
	private final String info;
	private final String translationKey;

	public StringTickStageExtra(String info, String translationKey)
	{
		this.info = info;
		this.translationKey = translationKey;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.s(this.tr(translationKey, info));
	}
}
