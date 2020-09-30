package carpettisaddition.commands;


import carpet.CarpetServer;
import carpet.CarpetSettings;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.RaidTracker;
import carpettisaddition.utils.Util;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.literal;

public class InfoCommand extends TranslatableCommand
{
	public static InfoCommand inst = new InfoCommand();

	public InfoCommand()
	{
		super("info");
	}
	private void __registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("info").
				requires((player) -> SettingsManager.canUseCommand(player, CarpetSettings.commandInfo)).
				then(literal("world").
						then(literal("tickorder").
								executes((c) -> showWorldTickOrder(c.getSource()))));
		dispatcher.register(builder);
	}
	public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		inst.__registerCommand(dispatcher);
	}

	private static int showWorldTickOrder(ServerCommandSource source)
	{
		int order = 0;
		for (World world : CarpetServer.minecraft_server.getWorlds())
		{
			order++;
			Messenger.m(source, Messenger.c(
					"g " + order + ". ",
					Util.getDimensionNameText(world.getDimension().getType())
			));
		}
		return 1;
	}
}
