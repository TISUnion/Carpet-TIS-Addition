package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.commands.lifetime.utils.AbstractReason;

public abstract class RemovalReason extends AbstractReason
{
	public RemovalReason()
	{
		super("removal_reason");
	}

	/**
	 * This removal reason doesn't mean the mob is removed from the world, but means the mob is removed from the mobcap
	 * If this method returns true, the removal reason will be ignored if lifeTimeTrackerConsidersMobcap is set to true
	 */
	public RemovalType getRemovalType()
	{
		return RemovalType.REMOVED_FROM_WORLD;
	}
}
