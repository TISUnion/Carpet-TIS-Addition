package carpettisaddition.settings.validator;

import carpet.settings.ParsedRule;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public class ValidationContext<T>
{
	@Nullable
	public final ServerCommandSource source;
	public final ParsedRule<T> rule;
	public final T value;
	public final String valueString;

	public ValidationContext(@Nullable ServerCommandSource source, ParsedRule<T> rule, T value, String valueString)
	{
		this.source = source;
		this.rule = rule;
		this.value = value;
		this.valueString = valueString;
	}

	public String ruleName()
	{
		return
				//#if MC >= 11901
				//$$ this.rule.name();
				//#else
				this.rule.name;
		//#endif
	}
}
