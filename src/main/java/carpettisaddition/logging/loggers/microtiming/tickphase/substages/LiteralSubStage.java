package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import net.minecraft.text.MutableText;

// TODO apply mixin
public class LiteralSubStage extends AbstractSubStage
{
	public static final LiteralSubStage ENTITY_WEATHER_EFFECT = new LiteralSubStage( "ticking_weather_effect_entities");
	public static final LiteralSubStage ENTITY_REGULAR = new LiteralSubStage("ticking_regular_entities");
	private final String translationKey;

	public LiteralSubStage(String translationKey)
	{
		this.translationKey = translationKey;
	}

	@Override
	public MutableText toText()
	{
		return tr(this.translationKey);
	}
}
