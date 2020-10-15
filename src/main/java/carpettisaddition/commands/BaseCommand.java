package carpettisaddition.commands;

import carpettisaddition.utils.TranslatableBase;


public abstract class BaseCommand extends TranslatableBase
{
	public BaseCommand(String name)
	{
		super("command", name);
	}
}
