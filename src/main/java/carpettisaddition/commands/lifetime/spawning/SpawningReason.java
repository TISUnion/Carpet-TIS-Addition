package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.commands.lifetime.utils.AbstractReason;

public abstract class SpawningReason extends AbstractReason
{
	public SpawningReason()
	{
		super("spawn_reason");
	}

	public SpawningType getSpawningType()
	{
		return SpawningType.ADDED_TO_WORLD;
	}
}
