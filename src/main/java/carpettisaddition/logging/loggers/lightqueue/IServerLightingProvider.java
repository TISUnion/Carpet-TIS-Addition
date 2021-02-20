package carpettisaddition.logging.loggers.lightqueue;

public interface IServerLightingProvider
{
	long getEnqueuedTaskCount();

	long getExecutedTaskCount();

	void resetCounter();

	long getQueueSize();
}
