package carpettisaddition.settings.validator;

import org.jetbrains.annotations.Nullable;

/**
 * A validator that always says ok == A rule change listener
 */
public abstract class RuleChangeListener<T> extends AbstractValidator<T>
{
	@Override
	protected final @Nullable T validate(ValidationContext<T> ctx)
	{
		return ctx.inputValue;
	}
}
