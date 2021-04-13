package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;

public enum TickStage
{
	SPAWNING("Spawning", true),
	SPAWNING_SPECIAL("SpawningSpecial", true),
	WORLD_BORDER("WorldBorder", true),
	TILE_TICK("TileTick", true),
	RAID("Raid", true),
	WANDERING_TRADER("WanderingTrader", true),
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

	private final String name;
	private final boolean insideWorld;

	TickStage(String name, boolean insideWorld)
	{
		this.name = name;
		this.insideWorld = insideWorld;
	}

	public String getName()
	{
		return name;
	}

	public String tr()
	{
		return MicroTimingLoggerManager.tr("stage." + this.name, this.name);
	}

	public boolean isInsideWorld()
	{
		return insideWorld;
	}
}
