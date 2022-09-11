package carpettisaddition.commands;

import carpettisaddition.translations.TranslationContext;

public abstract class AbstractCommand extends TranslationContext implements CommandRegister
{
	public AbstractCommand(String name)
	{
		super("command." + name);
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		throw new UnsupportedOperationException();
	}
}
