package carpettisaddition.commands;

import carpettisaddition.translations.TranslatableBase;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;


public abstract class AbstractCommand extends TranslatableBase
{
	public AbstractCommand(String name)
	{
		super("command", name);
	}

	public abstract void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher);
}
