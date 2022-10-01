package carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced;

import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import org.jetbrains.annotations.NotNull;

public class InstantBlockUpdaterReintroducedRuleListener extends RuleChangeListener<Boolean>
{
	@Override
	public void onRuleSet(ValidationContext<Boolean> ctx, @NotNull Boolean newValue)
	{
		InstantBlockUpdaterChanger.apply(newValue);
	}
}
