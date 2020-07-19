package carpettisaddition.interfaces;


import carpettisaddition.logging.logHelpers.RaidLogHelper;

public interface IRaid
{
	public void onRaidInvalidated(RaidLogHelper.InvalidateReason reason);
}
