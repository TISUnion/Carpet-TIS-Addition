/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.logging.loggers.portalCreation;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingAccess;
import carpettisaddition.utils.GameUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.entityfilter.EntityFilter;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalCreationLogger extends AbstractLogger
{
	public static final String NAME = "portalCreation";
	public static final ThreadLocal<Entity> entityThatCreatesThePortal = ThreadLocal.withInitial(() -> null);
	private static final PortalCreationLogger INSTANCE = new PortalCreationLogger();

	private PortalCreationLogger()
	{
		super(NAME, false);
	}

	public static PortalCreationLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return new String[]{"all", "players", "me", "@e[type=creeper]", "@e[type=!player]"};
	}

	public void onNetherPortalCreation(ServerWorld world, BlockPos portalPos, boolean floating)
	{
		if (!TISAdditionLoggerRegistry.__portalCreation)
		{
			return;
		}
		Entity entity = entityThatCreatesThePortal.get();
		if (entity == null)
		{
			return;
		}

		this.log((option, player) -> {
			if (!this.shouldReportFor(player, option, entity))
			{
				return null;
			}

			List<BaseText> lines = Lists.newArrayList();
			lines.add(pack(tr(
					"created",
					Messenger.formatting(Messenger.entity(entity), Formatting.BOLD),
					Messenger.dimensionColored(Messenger.coord(portalPos, DimensionWrapper.of(world)), DimensionWrapper.of(world)),
					Messenger.c(
							Messenger.s(GameUtils.getGameTime(), "q"),
							Messenger.s(" ", Formatting.DARK_GRAY),
							MicroTimingAccess.getTickPhase().toText("q")
					)
			)));
			return lines.toArray(new BaseText[0]);
		});
	}

	private BaseText pack(BaseText text)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", "m PC", "g ] "),
						Messenger.s(command),
						Messenger.ClickEvents.suggestCommand(command)
				),
				text
		);
	}

	private boolean shouldReportFor(PlayerEntity player, String option, Entity entity)
	{
		switch (option)
		{
			case "all":
				return true;
			case "me":
				return entity == player;
			case "players":
				return entity instanceof PlayerEntity;
			default:
				break;
		}
		return EntityFilter.createOptional(player, option).
				map(filter -> filter.test(entity)).
				orElse(false);
	}
}
