package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.text.BaseText;

public enum TickStage
{
	UNKNOWN("Unknown", false),
	SPAWNING("Spawning", true),
	SPAWNING_SPECIAL("SpawningSpecial", true),
	WORLD_BORDER("WorldBorder", true),
	TILE_TICK("TileTick", true),
	RAID("Raid", true),
	WANDERING_TRADER("WanderingTrader", true),  // included in SPAWNING_SPECIAL in 1.16
	BLOCK_EVENT("BlockEvent", true),
	ENTITY("Entity", true),
	CHUNK_TICK("ChunkTick", true),
	TILE_ENTITY("TileEntity", true),
	DRAGON_FIGHT("DragonFight", true),
	AUTO_SAVE("AutoSave", false),
	ASYNC_TASK("AsyncTask", false),
	PLAYER_ACTION("PlayerAction", false),
	COMMAND_FUNCTION("CommandFunction", false),
	NETWORK("Network", false),
	CONSOLE("Console", false),
	SCARPET("Scarpet", false);

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("stage");
	private final String translationKey;
	private final boolean insideWorld;

	TickStage(String name, boolean insideWorld)
	{
		this.translationKey = name.toLowerCase();
		this.insideWorld = insideWorld;
	}

	public BaseText getName()
	{
		return translator.tr(this.translationKey);
	}

	public boolean isInsideWorld()
	{
		return this.insideWorld;
	}
}
