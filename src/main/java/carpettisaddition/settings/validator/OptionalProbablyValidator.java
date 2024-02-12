package carpettisaddition.settings.validator;

/**
 * < 0 means disabled
 * >=0 means enabled
 */
public class OptionalProbablyValidator extends RangedNumberValidator<Double>
{
	public OptionalProbablyValidator()
	{
		super(-1.0, 1.0);
	}
}
