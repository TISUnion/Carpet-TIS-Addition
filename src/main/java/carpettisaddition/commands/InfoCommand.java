package carpettisaddition.commands;

import carpet.CarpetServer;
import carpet.CarpetSettings;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import java.util.List;

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
						then(literal("ticking_order").
								executes((c) -> showWorldTickOrder(c.getSource()))));
		dispatcher.register(builder);
	}
	public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		inst.__registerCommand(dispatcher);
	}

	private int showWorldTickOrder(ServerCommandSource source)
	{
		List<World> worlds = Lists.newArrayList(CarpetServer.minecraft_server.getWorlds());
		Messenger.m(source, Messenger.c(
				String.format("w %s", String.format(tr("ticking_order", "Ticking order of %d dimensions in the game:"), worlds.size()))
		));
		int order = 0;
		for (World world : worlds)
		{
			order++;
			Messenger.m(source, Messenger.c(
					"g " + order + ". ",
					Util.getDimensionNameText(world.getRegistryKey())
			));
		}
		return 1;
	}
}
