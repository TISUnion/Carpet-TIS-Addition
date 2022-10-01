package carpettisaddition.settings.validator;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractValidator<T> extends Validator<T>
{
	private static final Translator translator = new Translator("validator");

	protected static BaseText tr(String key, Object... args)
	{
		return translator.tr(key, args);
	}

	@Deprecated
	@Override
	public final T validate(ServerCommandSource source, ParsedRule<T> currentRule, T inputValue, String string)
	{
		ValidationContext<T> ctx = new ValidationContext<>(source, currentRule, inputValue, string);
		T newValue = this.validate(ctx);
		if (newValue != null)
		{
			this.onRuleSet(ctx, newValue);
			if (ctx.oldValue != newValue)
			{
				this.onRuleChanged(ctx, newValue);
			}
		}
		else
		{
			this.onValidationFailure(ctx);
		}
		return newValue;
	}

	@Deprecated
	@Override
	public final String description()
	{
		return null;
	}

	public void onRuleSet(ValidationContext<T> ctx, T newValue)
	{
	}

	public void onRuleChanged(ValidationContext<T> ctx, T newValue)
	{
	}

	public void onValidationFailure(ValidationContext<T> ctx)
	{
		Messenger.tell(
				ctx.source,
				Messenger.formatting(tr("basic.failure", ctx.ruleName(), ctx.inputValue), "r"),
				true
		);

		BaseText errorMessage = this.errorMessage(ctx);
		if (errorMessage != null)
		{
			Messenger.tell(ctx.source, Messenger.formatting(errorMessage, "r"), false);
		}
	}

	@Nullable
	protected abstract T validate(ValidationContext<T> ctx);

	public BaseText errorMessage(ValidationContext<T> ctx)
	{
		return null;
	}
}
