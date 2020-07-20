package carpettisaddition.commands;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.RaidTracker;
import carpettisaddition.mixins.command.RaidAccessor;
import carpettisaddition.mixins.command.RaidManagerAccessor;
import carpettisaddition.utils.Util;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.village.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.literal;


public class RaidCommand extends AbstractCommand
{
	public static RaidCommand inst = new RaidCommand();

	public RaidCommand()
	{
		super("raid");
	}

	private void __registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("raid")
			.requires((player) -> SettingsManager.canUseCommand(player, CarpetTISAdditionSettings.commandRaid))
			.then(literal("list")
					.executes((c) -> listRaid(c.getSource(), false))
					.then(literal("full")
							.executes((c) -> listRaid(c.getSource(), true))
					)
			)
			.then(literal("tracking")
					.executes((c) -> RaidTracker.printTrackingResult(c.getSource()))
					.then(literal("start")
							.executes((c) -> RaidTracker.startTracking(c.getSource()))
					)
					.then(literal("stop")
							.executes((c) -> RaidTracker.stopTracking(c.getSource()))
					)
					.then(literal("restart")
							.executes((c) -> RaidTracker.restartTracking(c.getSource()))
					)
					.then(literal("realtime")
							.executes((c) -> RaidTracker.printTrackingResult(c.getSource()))
					)
			);
		dispatcher.register(builder);
	}
	public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		inst.__registerCommand(dispatcher);
	}

	public int listRaid(ServerCommandSource source, boolean fullMode)
	{
		if (CarpetTISAdditionServer.minecraft_server == null)
		{
			return 0;
		}
		boolean hasRaid = false;
		for (ServerWorld world : CarpetTISAdditionServer.minecraft_server.getWorlds())
		{
			Map<Integer, Raid> raids = ((RaidManagerAccessor) world.getRaidManager()).getRaids();
			if (raids.isEmpty())
			{
				continue;
			}
			List<Object> result = new ArrayList<>();
			result.add(Util.getDimensionNameText(world.getDimensionRegistryKey()));
			result.add(Messenger.c(String.format("w  %s: %d", tr("raid count"), raids.size())));
			hasRaid |= raids.size() > 0;

			for (Map.Entry<Integer, Raid> entry : raids.entrySet())
			{
				Raid raid = entry.getValue();
				RaidAccessor raidAccessor = (RaidAccessor) raid;
				int currentWave = raidAccessor.getWavesSpawned();
				String status = raidAccessor.getStatus().getName();
				result.add(Messenger.c("g \n- ", Util.getTranslatedName("event.minecraft.raid"), String.format("w  #%d", raid.getRaidId())));
				result.add(Messenger.c("g \n  ", String.format("w %s: %s", tr("Status"), tr("status." + status, status))));
				result.add(Messenger.c("g \n  ", String.format("w %s: ", tr("Center")), Util.getCoordinateText("w", raid.getCenter(), world.getDimensionRegistryKey())));
				result.add(Messenger.c("g \n  ", String.format("w %s: %d", tr("Bad Omen Level"), raid.getBadOmenLevel())));
				result.add(Messenger.c("g \n  ", String.format("w %s: %d/%d", tr("Waves"), raidAccessor.getWavesSpawned(), raidAccessor.getWaveCount())));
				result.add(Messenger.c("g \n  ", String.format("w %s: ", tr("Raiders"))));
				Set<RaiderEntity> raiders = raidAccessor.getWaveToRaiders().get(currentWave);
				if (raiders == null || raiders.isEmpty())
				{
					result.add(Messenger.s(tr("None")));
				}
				else
				{
					int counter = 0;
					for (RaiderEntity raider : raiders)
					{
						BaseText raiderName = (BaseText)(raider.getType().getName().copy());
						Style nameStyle = Messenger.parseStyle(raider.equals(raidAccessor.getWaveToCaptain().get(currentWave)) ? "r" : "w");
						if (fullMode)
						{
							raiderName.setStyle(nameStyle);
						}
						BaseText raiderMessage = Messenger.c(
								raiderName,
								"g  @ ",
								Util.getCoordinateText("w", raider.getPos(), raider.world.getDimensionRegistryKey())
						);
						if (fullMode)
						{
							result.add(Messenger.c("g \n  - ", raiderMessage));
						}
						else
						{
							if (counter == 0)
							{
								result.add("w \n    ");
								counter = 10;
							}
							counter--;
							BaseText x = Messenger.s(String.format("[%s] ", Registry.ENTITY_TYPE.getId(raider.getType()).getPath().substring(0, 1).toUpperCase()));
							x.setStyle(nameStyle);
							x.setStyle(x.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, raiderMessage)));
							x.setStyle(x.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Util.getTeleportCommand(raider.getPos(), world.getDimensionRegistryKey()))));
							result.add(x);
						}
					}
				}
			}
			source.sendFeedback(Messenger.c(result.toArray(new Object[0])), false);
		}
		if (!hasRaid)
		{
			source.sendFeedback(Messenger.c(String.format("w %s", tr("no_raid", "No raid exists"))), false);
		}
		return 1;
	}
}
