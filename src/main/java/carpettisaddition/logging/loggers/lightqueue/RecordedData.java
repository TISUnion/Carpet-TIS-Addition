package carpettisaddition.logging.loggers.lightqueue;

public class RecordedData
{
	public final long enqueuedTask;
	public final long executedTask;
	public final long queueSize;

	public RecordedData(long enqueuedTask, long executedTask, long queueSize)
	{
		this.enqueuedTask = enqueuedTask;
		this.executedTask = executedTask;
		this.queueSize = queueSize;
	}
}
