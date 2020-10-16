package carpettisaddition.logging.loggers.microtick.enums;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;

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
	RANDOMTICK_CLIMATE("RandomTIck&Climate", true),
	TILE_ENTITY("TileEntity", true),
	AUTO_SAVE("AutoSave", false),
	PLAYER_ACTION("PlayerAction", false),
	COMMAND_FUNCTION("CommandFunction", false),
	NETWORK("Network", false);

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
		return MicroTickLoggerManager.tr("stage." + this.name, this.name);
	}

	public boolean isInsideWorld()
	{
		return insideWorld;
	}
}
