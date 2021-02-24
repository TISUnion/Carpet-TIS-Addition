package carpettisaddition.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegister
{
	void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher);
}
