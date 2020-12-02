package carpettisaddition.logging.loggers.commandblock;

public interface ICommandBlockExecutor
{
	long getLastLoggedTime();

	void setLastLoggedTime(long time);
}
