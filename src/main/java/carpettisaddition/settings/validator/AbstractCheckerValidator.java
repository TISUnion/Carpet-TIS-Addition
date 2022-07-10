package carpettisaddition.settings.validator;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractCheckerValidator<T> extends AbstractValidator<T>
{
	@Override
	protected final @Nullable T validate(ValidationContext<T> ctx)
	{
		return this.validateValue(ctx.value) ? ctx.value : null;
	}

	protected abstract boolean validateValue(T value);
}
