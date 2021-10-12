package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class LiteralSubStage extends AbstractSubStage
{
	public static final LiteralSubStage ENTITY_WEATHER_EFFECT = new LiteralSubStage("Ticking weather effect entities", "ticking_weather_effect_entities");
	public static final LiteralSubStage ENTITY_REGULAR = new LiteralSubStage("Ticking regular entities", "ticking_regular_entities");
	private final String info;
	private final String translationKey;

	public LiteralSubStage(String info, String translationKey)
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
