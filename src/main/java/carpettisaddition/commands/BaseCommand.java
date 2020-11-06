package carpettisaddition.commands;

import carpettisaddition.utils.TranslatableBase;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;


public abstract class BaseCommand extends TranslatableBase
{
	public BaseCommand(String name)
	{
		super("command", name);
	}

	public abstract void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher);
}
