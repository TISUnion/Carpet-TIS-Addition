package carpettisaddition.logging.loggers.lightqueue;

public class RecordedData
{
	public final long enqueuedTask;
	public final long executedTask;
	public final int queueSize;

	public RecordedData(long enqueuedTask, long executedTask, int queueSize)
	{
		this.enqueuedTask = enqueuedTask;
		this.executedTask = executedTask;
		this.queueSize = queueSize;
	}
}
