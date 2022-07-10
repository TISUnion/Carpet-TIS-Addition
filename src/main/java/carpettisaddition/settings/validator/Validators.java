package carpettisaddition.settings.validator;

import net.minecraft.text.BaseText;

public class Validators
{
	public static class NegativeNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() < 0;}
		@Override public BaseText errorMessage(ValidationContext<T> ctx) {return tr("negative_number.message");}
	}

	public static class PositiveNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() > 0;}
		@Override public BaseText errorMessage(ValidationContext<T> ctx) {return tr("positive_number.message");}
	}

	public static class NonNegativeNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() >= 0;}
		@Override public BaseText errorMessage(ValidationContext<T> ctx) {return tr("non_negative_number.message");}
	}

	public static class NonPositiveNumber<T extends Number> extends AbstractCheckerValidator<T>
	{
		@Override protected boolean validateValue(T value) {return value.doubleValue() <= 0;}
		@Override public BaseText errorMessage(ValidationContext<T> ctx) {return tr("positive_number.message");}
	}

	public static class Probability extends RangedNumberValidator<Double>
	{
		public Probability()
		{
			super(0.0D, 1.0D);
		}
	}
}
