package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.text.BaseText;

public class StringTickStageExtra extends TickStageExtraBase
{
	public static final StringTickStageExtra SYNC_TASKS = new StringTickStageExtra("SyncTasks including player actions", "sync_tasks");
	public static final StringTickStageExtra ENTITY_WEATHER_EFFECT = new StringTickStageExtra("Ticking weather effects", "entity_weather_effect");
	public static final StringTickStageExtra ENTITY_REGULAR = new StringTickStageExtra("Ticking regular entities", "entity_regular");
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
		return Messenger.s(MicroTimingLoggerManager.tr("stage_extra." + translationKey, info));
	}
}
