package carpettisaddition.utils.mixin.testers;

import carpettisaddition.utils.mixin.LithiumConfigAccess;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;

public abstract class BasicLithiumConditionTester implements ConditionTester
{
	private final boolean lithiumRuleEnabled;

	protected BasicLithiumConditionTester(String lithiumRule)
	{
		this.lithiumRuleEnabled = LithiumConfigAccess.isLithiumMixinRuleEnabled(lithiumRule);
	}

	public boolean isLithiumRuleEnabled()
	{
		return this.lithiumRuleEnabled;
	}

	@Override
	public boolean isSatisfied(String mixinClassName)
	{
		return this.isLithiumRuleEnabled();
	}
}
