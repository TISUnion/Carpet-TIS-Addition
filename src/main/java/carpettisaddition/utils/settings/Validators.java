package carpettisaddition.utils.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

public class Validators
{
	public abstract static class AbstractValidator<T> extends Validator<T>
	{
		// TODO smart translated message stuffs
	}

	public static class NonNegativeNumber<T extends Number> extends AbstractValidator<T>
	{
		@Override
		public T validate(ServerCommandSource source, ParsedRule<T> currentRule, T newValue, String string)
		{
			return newValue.doubleValue() >= 0 ? newValue : null;
		}
		@Override
		public String description() { return "Must be a positive number or 0";}
	}

	public static class Probablity<T extends Number> extends AbstractValidator<T>
	{
		@Override
		public T validate(ServerCommandSource source, ParsedRule<T> currentRule, T newValue, String string)
		{
			return (newValue.doubleValue() >= 0 && newValue.doubleValue() <= 1 )? newValue : null;
		}
		@Override
		public String description() { return "Must be between 0 and 1";}
	}
}
