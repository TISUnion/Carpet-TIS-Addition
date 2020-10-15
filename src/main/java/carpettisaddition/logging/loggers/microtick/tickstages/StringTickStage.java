package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import net.minecraft.text.BaseText;

public class StringTickStage implements ToTextAble
{
	public static final StringTickStage SYNC_TASKS = new StringTickStage("SyncTasks including player actions", "sync_tasks");
	public static final StringTickStage ENTITY_WEATHER_EFFECT = new StringTickStage("Ticking weather effects", "entity_weather_effect");
	public static final StringTickStage ENTITY_REGULAR = new StringTickStage("Ticking regular entities", "entity_regular");
	private final String info;
	private final String translationKey;

	public StringTickStage(String info, String translationKey)
	{
		this.info = info;
		this.translationKey = translationKey;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.s(MicroTickLoggerManager.tr("stage_extra." + translationKey, info));
	}
}
