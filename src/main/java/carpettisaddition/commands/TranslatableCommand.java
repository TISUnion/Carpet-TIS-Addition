package carpettisaddition.commands;

import carpettisaddition.utils.TranslatableBase;


public abstract class TranslatableCommand extends TranslatableBase
{
	public TranslatableCommand(String name)
	{
		super("command", name);
	}
}
