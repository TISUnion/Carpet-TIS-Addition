package carpettisaddition.commands;

import carpettisaddition.utils.TranslatableBase;


public abstract class AbstractCommand extends TranslatableBase
{
	public AbstractCommand(String name)
	{
		super("command", name);
	}
}
