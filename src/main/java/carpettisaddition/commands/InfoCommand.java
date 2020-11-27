package carpettisaddition.commands;

import carpet.CarpetSettings;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class InfoCommand extends AbstractCommand
{
	private static final InfoCommand INSTANCE = new InfoCommand();

	public static InfoCommand getInstance()
	{
		return INSTANCE;
	}

	public InfoCommand()
	{
		super("info");
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("info").
				requires((player) -> CarpetModUtil.canUseCommand(player, CarpetSettings.commandInfo)).
				then(literal("world").
						then(literal("ticking_order").
								executes((c) -> showWorldTickOrder(c.getSource()))));
		dispatcher.register(builder);
	}

	private int showWorldTickOrder(ServerCommandSource source)
	{
		List<World> worlds = Lists.newArrayList(CarpetTISAdditionServer.minecraft_server.getWorlds());
		Messenger.m(source, Messenger.c(
				String.format("w %s", String.format(tr("ticking_order", "Ticking order of %d dimensions in the game:"), worlds.size()))
		));
		int order = 0;
		for (World world : worlds)
		{
			order++;
			Messenger.m(source, Messenger.c(
					"g " + order + ". ",
					TextUtil.getDimensionNameText(world.getRegistryKey())
			));
		}
		return 1;
	}
}
