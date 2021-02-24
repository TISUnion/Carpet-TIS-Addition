package carpettisaddition.commands;

import carpettisaddition.translations.TranslatableBase;

public abstract class AbstractCommand extends TranslatableBase implements CommandRegister
{
	public AbstractCommand(String name)
	{
		super("command", name);
	}
}
