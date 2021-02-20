package carpettisaddition.logging.loggers.lightqueue;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Queues;

import java.util.Deque;

public class WindowedDataRecorder
{
	private final Deque<RecordedData> dataQueue = Queues.newArrayDeque();
	private long enqueuedCount;
	private long executedCount;

	public void add(RecordedData newData)
	{
		this.enqueuedCount += newData.enqueuedTask;
		this.executedCount += newData.executedTask;
		this.dataQueue.addLast(newData);
		while (this.dataQueue.size() > CarpetTISAdditionSettings.lightQueueLoggerSamplingDuration)
		{
			RecordedData data = dataQueue.removeFirst();
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
}
