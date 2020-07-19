package carpettisaddition.commands;

import carpettisaddition.utils.TranslatableBase;


public abstract class CommandBase extends TranslatableBase
{
	public CommandBase(String name)
	{
		super("command", name);
	}
}
