package carpettisaddition.commands;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.RaidTracker;
import carpettisaddition.mixins.command.RaidAccessor;
import carpettisaddition.mixins.command.RaidManagerAccessor;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.registry.Registry;

import java.util.*;

import static net.minecraft.server.command.CommandManager.literal;


public class RaidCommand extends TranslatableCommand
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
			List<BaseText> result = new ArrayList<>();
			result.add(Messenger.c(Util.getDimensionNameText(world.getDimension().getType()), String.format("w  %s: %d", tr("raid count"), raids.size())));
			hasRaid |= raids.size() > 0;

			for (Map.Entry<Integer, Raid> entry : raids.entrySet())
			{
				Raid raid = entry.getValue();
				RaidAccessor raidAccessor = (RaidAccessor) raid;
				int currentWave = raidAccessor.getWavesSpawned();
				String status = raidAccessor.getStatus().getName();
				result.add(Messenger.c("g - ", Util.getTranslatedName("event.minecraft.raid"), String.format("w  #%d", raid.getRaidId())));
				result.add(Messenger.c("g   ", String.format("w %s: %s", tr("Status"), tr("status." + status, status))));
				if (fullMode)
				{
					result.add(Messenger.c("g   ", String.format("w %s: ", tr("Center")), Util.getCoordinateText("w", raid.getCenter(), world.getDimension())));
					result.add(Messenger.c("g   ", String.format("w %s: %d", tr("Bad Omen Level"), raid.getBadOmenLevel())));
				}
				result.add(Messenger.c("g   ", String.format("w %s: %d/%d", tr("Waves"), raidAccessor.getWavesSpawned(), raidAccessor.getWaveCount())));

				Set<RaiderEntity> raiders = raidAccessor.getWaveToRaiders().get(currentWave);
				boolean hasRaiders = raiders != null && !raiders.isEmpty();
				result.add(Messenger.c("g   ", String.format("w %s: %s", tr("Raiders"), hasRaiders ? String.format("x%d", raiders.size()) : tr("None"))));
				if (hasRaiders)
				{
					int counter = 0;
					List<Object> line = Lists.newArrayList();
					for (Iterator<RaiderEntity> iter = raiders.iterator(); iter.hasNext(); )
					{
						RaiderEntity raider = iter.next();
						BaseText raiderName = Util.getEntityText(raider.equals(raidAccessor.getWaveToCaptain().get(currentWave)) ? "r" : "w", raider);
						BaseText raiderMessage = Messenger.c(
								raiderName,
								"g  @ ",
								Util.getCoordinateText("w", raider.getPos(), raider.world.getDimension())
						);
						if (fullMode)
						{
							result.add(Messenger.c("g   - ", raiderMessage));
						}
						else
						{
							BaseText x = Messenger.s(String.format("[%s] ", Registry.ENTITY_TYPE.getId(raider.getType()).getPath().substring(0, 1).toUpperCase()));
							x.setStyle(raiderName.getStyle().copy());
							x.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, raiderMessage));
							x.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Util.getTeleportCommand(raider)));
							line.add(x);
							counter++;
							if (counter == 10 || !iter.hasNext())
							{
								line.add(0, "w     ");
								result.add(Messenger.c(line.toArray(new Object[0])));
								line.clear();
								counter = 0;
							}
						}
					}
				}
			}
			Messenger.send(source, result);
		}
		if (!hasRaid)
		{
			source.sendFeedback(Messenger.c(String.format("w %s", tr("no_raid", "No raid exists"))), false);
		}
		return 1;
	}
}
