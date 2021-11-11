package carpettisaddition.commands.raid;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.mixins.command.raid.RaidAccessor;
import carpettisaddition.mixins.command.raid.RaidManagerAccessor;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.raid.Raid;

import java.util.*;

import static net.minecraft.server.command.CommandManager.literal;


public class RaidCommand extends AbstractCommand
{
	private static final RaidCommand INSTANCE = new RaidCommand();

	public static RaidCommand getInstance()
	{
		return INSTANCE;
	}

	public RaidCommand()
	{
		super("raid");
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("raid")
			.requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRaid))
			.then(literal("list")
					.executes((c) -> listRaid(c.getSource(), false))
					.then(literal("full")
							.executes((c) -> listRaid(c.getSource(), true))
					)
			)
			.then(RaidTracker.getInstance().getTrackingArgumentBuilder());
		dispatcher.register(builder);
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
			result.add(Messenger.c(
					Messenger.dimension(DimensionWrapper.of(world)),
					"w  ", tr("raid_count", raids.size())
			));
			hasRaid |= raids.size() > 0;

			for (Map.Entry<Integer, Raid> entry : raids.entrySet())
			{
				Raid raid = entry.getValue();
				RaidAccessor raidAccessor = (RaidAccessor) raid;
				int currentWave = raidAccessor.getWavesSpawned();
				String status = raidAccessor.getStatus().getName();
				result.add(Messenger.c("g - ", Messenger.tr("event.minecraft.raid"), String.format("w  #%d", raid.getRaidId())));
				result.add(Messenger.c("g   ", tr("status"), "w : ", tr("status." + status)));
				if (fullMode)
				{
					result.add(Messenger.c("g   ", tr("center"), "w : ", Messenger.coord("w", raid.getCenter(), DimensionWrapper.of(world))));
					result.add(Messenger.c("g   ", tr("bad_omen_level"), "w : ", Messenger.s(String.valueOf(raid.getBadOmenLevel()))));
				}
				result.add(Messenger.c("g   ", tr("waves"), "w : ", String.format("w %d/%d", raidAccessor.getWavesSpawned(), raidAccessor.getWaveCount())));

				Set<RaiderEntity> raiders = raidAccessor.getWaveToRaiders().get(currentWave);
				boolean hasRaiders = raiders != null && !raiders.isEmpty();
				result.add(Messenger.c("g   ", tr("raiders"), "w : ", hasRaiders ? Messenger.s(String.format("x%d", raiders.size())) : tr("none")));
				if (hasRaiders)
				{
					int counter = 0;
					List<Object> line = Lists.newArrayList();
					for (Iterator<RaiderEntity> iter = raiders.iterator(); iter.hasNext(); )
					{
						RaiderEntity raider = iter.next();
						BaseText raiderName = Messenger.entity(raider.equals(raidAccessor.getWaveToCaptain().get(currentWave)) ? "r" : "w", raider);
						BaseText raiderMessage = Messenger.c(
								raiderName,
								"g  @ ",
								Messenger.coord("w", raider.getPos(), DimensionWrapper.of(raider.world))
						);
						if (fullMode)
						{
							result.add(Messenger.c("g   - ", raiderMessage));
						}
						else
						{
							BaseText x = Messenger.s(String.format("[%s] ", Registry.ENTITY_TYPE.getId(raider.getType()).getPath().substring(0, 1).toUpperCase()));
							x.setStyle(raiderName.getStyle());
							Messenger.hover(x, raiderMessage);
							Messenger.click(x, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(raider)));
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
			Messenger.tell(source, result);
		}
		if (!hasRaid)
		{
			Messenger.tell(source, tr("no_raid"));
		}
		return 1;
	}
}
