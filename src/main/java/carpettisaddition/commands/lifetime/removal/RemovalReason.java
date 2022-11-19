package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.commands.lifetime.utils.AbstractReason;

public abstract class RemovalReason extends AbstractReason
{
	public RemovalReason()
	{
		super("removal_reason");
	}

	public RemovalType getRemovalType()
	{
		return RemovalType.REMOVED_FROM_WORLD;
	}
}
