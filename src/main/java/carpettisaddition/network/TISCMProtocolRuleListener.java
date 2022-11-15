package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.settings.validator.RuleChangeListener;
import carpettisaddition.settings.validator.ValidationContext;
import carpettisaddition.utils.Messenger;
import net.minecraft.util.Formatting;

/**
 * A rule value listener for those feature rules using TISCM protocol
 * It will show a warning message if the feature rule is on but the rule tiscmNetworkProtocol is off
 */
public class TISCMProtocolRuleListener extends RuleChangeListener<Boolean>
{
	@Override
	public void onRuleSet(ValidationContext<Boolean> ctx, Boolean newValue)
	{
		if (ctx.source != null && newValue && !ctx.ruleName().equals("tiscmNetworkProtocol"))
		{
			if (!CarpetTISAdditionSettings.tiscmNetworkProtocol)
			{
				Messenger.tell(ctx.source, Messenger.formatting(
						tr("tiscm_protocol.protocol_not_enabled", ctx.ruleName()),
						Formatting.GRAY, Formatting.ITALIC
				));
			}
		}
	}
}
