package carpettisaddition.settings.validator;

import net.minecraft.text.BaseText;

public abstract class RangedNumberValidator<T extends Number> extends AbstractCheckerValidator<T>
{
	protected final T lowerBound;
	protected final T upperBound;

	public RangedNumberValidator(T lowerBound, T upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	protected boolean validateValue(T value)
	{
		return this.lowerBound.doubleValue() <= value.doubleValue() && value.doubleValue() <= this.upperBound.doubleValue();
	}

	@Override
	public BaseText errorMessage(ValidationContext<T> ctx)
	{
		return tr("ranged_number.message", this.lowerBound, this.upperBound);
	}
}
