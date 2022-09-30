package carpettisaddition.utils;

public class MiscUtil
{
	public static Thread startThread(String threadName, Runnable runnable)
	{
		Thread thread = new Thread(runnable);
		if (threadName != null)
		{
			thread.setName(threadName);
		}
		thread.setDaemon(true);
		thread.start();
		return thread;
	}

	public static void assertTrue(boolean value)
	{
		if (!value)
		{
			throw new IllegalStateException("Assertion failed");
		}
	}
}
