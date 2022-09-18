package carpettisaddition.settings.validator;

import org.jetbrains.annotations.Nullable;

/**
 * A validator that always says ok
 */
public class DefaultValidator<T> extends AbstractValidator<T>
{
	@Override
	protected @Nullable T validate(ValidationContext<T> ctx)
	{
		return ctx.value;
	}
}
