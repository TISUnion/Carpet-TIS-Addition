package carpettisaddition.commands;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static carpettisaddition.CarpetTISAdditionServer.*;

public class CarpetTisAdditionCommand
{
	public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(compactName)
				.requires((player) -> player.hasPermissionLevel(2))
				.executes((context) -> showVersion(context.getSource()));
		dispatcher.register(builder);
	}

	private static int showVersion(ServerCommandSource source)
	{
		Messenger.m(source,
				String.format("wb %s ", fancyName),
				String.format("w %s: ", "version"),
				String.format("d %s", version)
		);
		return 1;
	}
}
