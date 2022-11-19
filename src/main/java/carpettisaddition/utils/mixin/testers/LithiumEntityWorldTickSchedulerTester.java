package carpettisaddition.utils.mixin.testers;

public class LithiumEntityWorldTickSchedulerTester extends BasicLithiumConditionTester
{
	public LithiumEntityWorldTickSchedulerTester()
	{
		super("world.tick_scheduler");
	}

	public static class Inverted extends LithiumEntityWorldTickSchedulerTester
	{
		@Override
		public boolean isLithiumRuleEnabled()
		{
			return !super.isLithiumRuleEnabled();
		}
	}
}