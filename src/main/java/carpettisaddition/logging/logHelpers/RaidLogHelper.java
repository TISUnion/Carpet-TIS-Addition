package carpettisaddition.logging.logHelpers;

import net.minecraft.entity.raid.Raid;

public class RaidLogHelper extends AbstractLogHelper
{
	private Raid raid;
	private InvalidateReason invalidateReason = null;

	public RaidLogHelper(Raid raid)
	{
		super("raid");
		this.raid = raid;
	}

	public void onRaidCreated()
	{

	}

	public void onRaiderSpawned()
	{

	}

	public void onRaidInvalidated(InvalidateReason reason)
	{
		this.invalidateReason = reason;
	}

	public void onRaidRemoved()
	{

	}

	public static enum InvalidateReason
	{
		DIFFICULTY_PEACEFUL,
		GAMERULE_DISABLE,
		POI_NOT_FOUND,
		TIME_OUT,
		RAIDER_CANNOT_SPAWN,
		FINISHED
	}
}
