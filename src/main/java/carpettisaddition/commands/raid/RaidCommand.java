/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.raid;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.mixins.command.raid.RaidAccessor;
import carpettisaddition.mixins.command.raid.RaidManagerAccessor;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.IdentifierUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;

import java.util.*;

import static net.minecraft.commands.Commands.literal;

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
	public void registerCommand(CommandTreeContext.Register context)
	{
		LiteralArgumentBuilder<CommandSourceStack> builder = literal("raid")
			.requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRaid))
			.then(literal("list")
					.executes((c) -> listRaid(c.getSource(), false))
					.then(literal("full")
							.executes((c) -> listRaid(c.getSource(), true))
					)
			)
			.then(RaidTracker.getInstance().createTrackingArgumentBuilder());
		context.dispatcher.register(builder);
	}

	public int listRaid(CommandSourceStack source, boolean fullMode)
	{
		if (CarpetTISAdditionServer.minecraft_server == null)
		{
			return 0;
		}
		boolean hasRaid = false;
		for (ServerLevel world : CarpetTISAdditionServer.minecraft_server.getAllLevels())
		{
			Map<Integer, Raid> raids = ((RaidManagerAccessor) world.getRaids()).getRaids();
			if (raids.isEmpty())
			{
				continue;
			}
			List<BaseComponent> result = new ArrayList<>();
			result.add(Messenger.c(
					Messenger.dimension(DimensionWrapper.of(world)),
					"w  ", tr("raid_count", raids.size())
			));
			hasRaid |= raids.size() > 0;

			for (Map.Entry<Integer, Raid> entry : raids.entrySet())
			{
				Raid raid = entry.getValue();
				int raidId = ((RaidWithIdAndWorld)raid).getRaidId$TISCM();
				RaidAccessor raidAccessor = (RaidAccessor) raid;
				int currentWave = raidAccessor.getWavesSpawned();
				String status = raidAccessor.getStatus()
						//#if MC >= 12105
						//$$ .getSerializedName();
						//#else
						.getName();
						//#endif
				result.add(Messenger.c("g - ", Messenger.tr("event.minecraft.raid"), String.format("w  #%d", raidId)));
				result.add(Messenger.c("g   ", tr("status"), "w : ", tr("status." + status)));
				result.add(Messenger.c("g   ", tr("center"), "w : ", Messenger.coord("w", raid.getCenter(), DimensionWrapper.of(world))));
				result.add(Messenger.c("g   ", tr("bad_omen_level"), "w : ", Messenger.s(raid.getBadOmenLevel())));
				result.add(Messenger.c("g   ", tr("waves"), "w : ", String.format("w %d/%d", raidAccessor.getWavesSpawned(), raidAccessor.getWaveCount())));

				Set<Raider> raiders = raidAccessor.getWaveToRaiders().get(currentWave);
				boolean hasRaiders = raiders != null && !raiders.isEmpty();
				result.add(Messenger.c("g   ", tr("raiders"), "w : ", hasRaiders ? Messenger.s(String.format("x%d", raiders.size())) : tr("none")));
				if (hasRaiders)
				{
					int counter = 0;
					List<Object> line = Lists.newArrayList();
					for (Iterator<Raider> iter = raiders.iterator(); iter.hasNext(); )
					{
						Raider raider = iter.next();
						BaseComponent raiderName = Messenger.entity(raider.equals(raidAccessor.getWaveToCaptain().get(currentWave)) ? "r" : "w", raider);
						BaseComponent raiderMessage = Messenger.c(
								raiderName,
								"g  @ ",
								Messenger.coord("w", raider.position(), DimensionWrapper.of(raider))
						);
						if (fullMode)
						{
							result.add(Messenger.c("g   - ", raiderMessage));
						}
						else
						{
							BaseComponent x = Messenger.s(String.format("[%s] ", IdentifierUtils.id(raider.getType()).getPath().substring(0, 1).toUpperCase()));
							x.setStyle(
									//#if MC >= 11600
									//$$ raiderName.getStyle()
									//#else
									raiderName.getStyle().flatCopy()
									//#endif
							);
							Messenger.hover(x, raiderMessage);
							Messenger.click(x, Messenger.ClickEvents.suggestCommand(TextUtils.tp(raider)));
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
