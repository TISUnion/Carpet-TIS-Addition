package carpettisaddition.settings.validator;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractCheckerValidator<T> extends AbstractValidator<T>
{
	@Override
	protected final @Nullable T validate(ValidationContext<T> ctx)
	{
		boolean result;
		try
		{
			result = this.validateValue(ctx.value);
		}
		catch (UnsupportedOperationException e)
		{
			result = this.validateContext(ctx);
		}
		return result ? ctx.value : null;
	}

	// Implement one of the following methods

	protected boolean validateContext(ValidationContext<T> ctx)
	{
		throw new UnsupportedOperationException();
	}

	protected boolean validateValue(T value)
	{
		throw new UnsupportedOperationException();
	}
}
