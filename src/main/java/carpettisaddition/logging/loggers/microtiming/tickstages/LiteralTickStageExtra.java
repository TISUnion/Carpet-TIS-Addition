package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class LiteralTickStageExtra extends TickStageExtraBase
{
	public static final LiteralTickStageExtra ENTITY_WEATHER_EFFECT = new LiteralTickStageExtra("Ticking weather effect entities", "ticking_weather_effect_entities");
	public static final LiteralTickStageExtra ENTITY_REGULAR = new LiteralTickStageExtra("Ticking regular entities", "ticking_regular_entities");
	private final String info;
	private final String translationKey;

	public LiteralTickStageExtra(String info, String translationKey)
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
