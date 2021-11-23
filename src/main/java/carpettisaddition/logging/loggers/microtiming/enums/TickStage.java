package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.text.BaseText;

public enum TickStage
{
	UNKNOWN(false),
	SPAWNING(true),
	SPAWNING_SPECIAL(true),
	WORLD_BORDER(true),
	TILE_TICK(true),
	RAID(true),
	WANDERING_TRADER(true),  // not used. it's included in SPAWNING_SPECIAL in 1.16
	BLOCK_EVENT(true),
	ENTITY(true),
	CHUNK_TICK(true),
	TILE_ENTITY(true),
	DRAGON_FIGHT(true),
	AUTO_SAVE(false),
	ASYNC_TASK(false),
	PLAYER_ACTION(false),
	COMMAND_FUNCTION(false),
	NETWORK(false),
	CONSOLE(false),
	SCARPET(false);

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("stage");
	private final String translationKey;
	private final boolean insideWorld;

	TickStage(boolean insideWorld)
	{
		this.translationKey = this.name().toLowerCase();
		this.insideWorld = insideWorld;
	}

	public BaseText toText()
	{
		return translator.tr(this.translationKey);
	}

	public boolean isInsideWorld()
	{
		return this.insideWorld;
	}
}
