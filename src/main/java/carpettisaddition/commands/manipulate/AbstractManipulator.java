package carpettisaddition.commands.manipulate;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.translations.TranslationContext;

public abstract class AbstractManipulator extends TranslationContext
{
	private final String name;

	protected AbstractManipulator(String name)
	{
		super(ManipulateCommand.getInstance().getTranslator().getDerivedTranslator(name));
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public abstract void buildSubCommand(CommandTreeContext.Node context);
}
