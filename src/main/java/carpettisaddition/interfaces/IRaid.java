package carpettisaddition.interfaces;


import carpettisaddition.logging.loggers.RaidLogger;

public interface IRaid
{
	public void onRaidInvalidated(RaidLogger.InvalidateReason reason);
}
