package carpettisaddition.logging.loggers.microtiming.utils;

public class MicroTimingMixinGlobals
{
	public static ThreadLocal<MicroTimingMixinGlobals.ContinuouslyEvent> prevStateUpdateEvent = ThreadLocal.withInitial(() -> null);

	public interface ContinuouslyEvent
	{
		void setIsStart(boolean isStart);
		void setIsEnd(boolean isEnd);
	}
}
