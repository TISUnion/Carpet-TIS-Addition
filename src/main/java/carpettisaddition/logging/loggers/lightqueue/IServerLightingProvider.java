package carpettisaddition.logging.loggers.lightqueue;

public interface IServerLightingProvider
{
	long getEnqueuedTaskCountAndClean();

	long getExecutedTaskCountAndClean();

	long getQueueSize();
}
