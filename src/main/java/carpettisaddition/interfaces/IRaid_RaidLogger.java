package carpettisaddition.interfaces;


import carpettisaddition.logging.loggers.RaidLogger;

public interface IRaid_RaidLogger
{
	void onRaidInvalidated(RaidLogger.InvalidateReason reason);
}
