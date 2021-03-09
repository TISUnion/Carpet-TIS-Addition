package carpettisaddition.logging.loggers.lightqueue;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Queues;

import java.util.Deque;

public class WindowedDataRecorder
{
	private final Deque<RecordedData> dataQueue = Queues.newArrayDeque();
	private long enqueuedCount = 0;
	private long executedCount = 0;

	public void add(RecordedData newData)
	{
		this.enqueuedCount += newData.enqueuedTask;
		this.executedCount += newData.executedTask;
		this.dataQueue.addLast(newData);
		while (this.dataQueue.size() > CarpetTISAdditionSettings.lightQueueLoggerSamplingDuration)
		{
			RecordedData data = this.dataQueue.removeFirst();
			this.enqueuedCount -= data.enqueuedTask;
			this.executedCount -= data.executedTask;
		}
	}

	public Deque<RecordedData> getQueue()
	{
		return this.dataQueue;
	}

	public long getEnqueuedCount()
	{
		return this.enqueuedCount;
	}

	public long getExecutedCount()
	{
		return this.executedCount;
	}

	public void clear()
	{
		this.dataQueue.clear();
		this.enqueuedCount = 0;
		this.executedCount = 0;
	}
}
