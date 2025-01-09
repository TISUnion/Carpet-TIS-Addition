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

package carpettisaddition.logging.loggers.ticket;

//#if MC < 11500
//$$ import carpettisaddition.logging.compat.ExtensionLogger;
//#endif

import carpet.logging.Logger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import com.google.common.collect.Lists;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

/**
 * Raw type {@link ChunkTicketType} usages here are to make the version compatibility earlier
 * Since in MC 1.21.5+, {@link ChunkTicketType} are no longer generic
 */
//#if MC < 12105
@SuppressWarnings("rawtypes")
//#endif
public class TicketLogger extends AbstractLogger
{
	public static final String NAME = "ticket";
	private static final TicketLogger INSTANCE = new TicketLogger();

	private final List<ChunkTicketType> tickTypes = Lists.newArrayList();

	public TicketLogger()
	{
		super(NAME, false);
	}

	public static TicketLogger getInstance()
	{
		return INSTANCE;
	}

	public void addTicketType(ChunkTicketType ticketType)
	{
		this.tickTypes.add(ticketType);
	}

	private String[] getLoggingSuggestions()
	{
		List<String> suggestions = this.tickTypes.stream().map(ChunkTicketType::toString).collect(Collectors.toList());
		suggestions.add(createCompoundOption(
				ChunkTicketType.PORTAL.toString(),
				//#if MC >= 12105
				//$$ ChunkTicketType.PLAYER_SIMULATION.toString()
				//#else
				ChunkTicketType.PLAYER.toString()
				//#endif
		));
		suggestions.add(createCompoundOption(ChunkTicketType.PORTAL.toString(), ChunkTicketType.DRAGON.toString()));
		return wrapOptions(suggestions.toArray(new String[0]));
	}

	@Override
	public Logger createCarpetLogger()
	{
		return new
				//#if MC < 11500
				//$$ ExtensionLogger
				//#else
				Logger
				//#endif
				(
						TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, ChunkTicketType.PORTAL.toString(), null
						//#if MC >= 11700
						//$$ , false
						//#endif
				)
		{
			@Override
			public String[] getOptions()
			{
				return TicketLogger.this.getLoggingSuggestions();
			}
		};
	}

	private String formatSize(int range)
	{
		range = max(range, 0);
		int length = range * 2 - 1;
		return String.format("%d * %d", length, length);
	}

	private void onManipulateTicket(ServerWorld world, long position, ChunkTicket chunkTicket, ActionType actionType)
	{
		this.log((option) ->
		{
			ChunkTicketType chunkTicketType = chunkTicket.getType();
			if (Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(chunkTicketType.toString()))
			{
				long expiryTicks = chunkTicketType.getExpiryTicks();
				ChunkPos pos = new ChunkPos(position);
				int level = chunkTicket.getLevel();

				return new BaseText[]{Messenger.c(
						Messenger.fancy(
								Messenger.s(String.format("[%s] ", world.getTime()), "g"),
								tr("time_detail", DimensionWrapper.of(world).getIdentifierString(), world.getTime()),
								null
						),
						tr("message",
								Messenger.fancy(
										Messenger.s(chunkTicketType.toString(), "d"),
										tr(
												"ticket_detail",
												chunkTicket.getLevel(), expiryTicks > 0 ? Messenger.s(expiryTicks + " gt") : tr("permanent"),
												formatSize(32 - level), formatSize(33 - level), formatSize(34 - level)
										),
										null
								),
								actionType.getText(this.getTranslator()),
								Messenger.coord(pos, DimensionWrapper.of(world))
						),
						"w  ",
						StackTracePrinter.makeSymbol(this.getClass())
				)};
			}
			else
			{
				return null;
			}
		});
	}

	public static void onAddTicket(ServerWorld world, long position, ChunkTicket chunkTicket)
	{
		if (TISAdditionLoggerRegistry.__ticket)
		{
			INSTANCE.onManipulateTicket(world, position, chunkTicket, ActionType.ADD);
		}
	}

	public static void onRemoveTicket(ServerWorld world, long position, ChunkTicket chunkTicket)
	{
		if (TISAdditionLoggerRegistry.__ticket)
		{
			INSTANCE.onManipulateTicket(world, position, chunkTicket, ActionType.REMOVE);
		}
	}

	private enum ActionType
	{
		ADD("l", "added"),
		REMOVE("r", "removed");

		private final String color;
		private final String translation;

		ActionType(String color, String translation)
		{
			this.color = color;
			this.translation = translation;
		}

		private BaseText getText(Translator translator)
		{
			return Messenger.formatting(translator.tr(this.translation), this.color);
		}
	}
}
