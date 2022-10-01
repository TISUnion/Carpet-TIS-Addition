package carpettisaddition.logging.loggers.microtiming.utils;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class MicroTimingRuleListener extends RuleChangeListener<Boolean>
{
	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("rule_listener");

	@Override
	public void onRuleChanged(ValidationContext<Boolean> ctx, @NotNull Boolean newValue)
	{
		if (ctx.source != null && ctx.source.getWorld() != null && newValue)
		{
			if (!MicroTimingUtil.isBlockUpdateInstant(ctx.source.getWorld()))
			{
				String ruleName = "instantBlockUpdaterReintroduced";
				BaseText ruleText = Messenger.fancy(
						Messenger.s(ruleName),
						translator.tr("click_to_see_rule"),
						new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/carpet " + ruleName)
				);
				Messenger.tell(ctx.source, Messenger.formatting(
						translator.tr("instant_block_updater_promote", ruleText),
						Formatting.GRAY, Formatting.ITALIC
				));
			}
		}
	}
}
