package carpettisaddition.settings.validator;

/**
 * < 0 means disabled
 * >=0 means enabled
 */
public class OptionalPercentValidator extends RangedNumberValidator<Integer>
{
	public OptionalPercentValidator()
	{
		super(-1, 100);
	}
}
