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
import carpettisaddition.utils.PositionUtils;
import carpettisaddition.utils.WorldUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import com.google.common.collect.Lists;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.level.ChunkPos;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;

//#if MC >= 12105
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//#endif

/**
 * Raw type {@link TicketType} usages here are to make the version compatibility earlier
 * Since in MC 1.21.5+, {@link TicketType} are no longer generic
 */
//#if MC < 12105
@SuppressWarnings("rawtypes")
//#endif
public class TicketLogger extends AbstractLogger
{
	public static final String NAME = "ticket";
	private static final TicketLogger INSTANCE = new TicketLogger();

	// "portal", "dragon", "my_namespace:foo_ticket"
	private final List<String> allTicketNames = Lists.newArrayList();

	public TicketLogger()
	{
		super(NAME, false);
	}

	public static TicketLogger getInstance()
	{
		return INSTANCE;
	}

	public void addVanillaTicketName(String ticketName)
	{
		this.allTicketNames.add(ticketName);
	}

	private String nameOf(TicketType type)
	{
		//#if MC >= 12105
		//$$ var id = BuiltInRegistries.TICKET_TYPE.getKey(type);
		//$$ if (id == null)
		//$$ {
		//$$ 	return "[unregistered]";  // the same as net.minecraft.Util#getRegisteredName
		//$$ }
		//$$ if (id.getNamespace().equals("minecraft"))
		//$$ {
		//$$ 	return id.getPath();
		//$$ }
		//$$ return id.toString();
		//#else
		return type.toString();
		//#endif
	}

	private String[] getLoggingSuggestions()
	{
		List<String> suggestions = Lists.newArrayList(allTicketNames);
		suggestions.add(createCompoundOption(
				nameOf(TicketType.PORTAL),
				//#if MC >= 12105
				//$$ nameOf(TicketType.PLAYER_SIMULATION)
				//#else
				nameOf(TicketType.PLAYER)
				//#endif
		));
		suggestions.add(createCompoundOption(nameOf(TicketType.PORTAL), nameOf(TicketType.DRAGON)));
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
						TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, nameOf(TicketType.PORTAL), null
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

	private void onManipulateTicket(ServerLevel world, long position, Ticket chunkTicket, ActionType actionType)
	{
		this.log((option) ->
		{
			TicketType chunkTicketType = chunkTicket.getType();
			if (Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(nameOf(chunkTicketType)))
			{
				long expiryTicks = chunkTicketType.timeout();
				ChunkPos pos = PositionUtils.unpackChunkPos(position);
				int level = chunkTicket.getTicketLevel();

				return new BaseComponent[]{Messenger.c(
						Messenger.fancy(
								Messenger.s(String.format("[%s] ", WorldUtils.getWorldTime(world)), "g"),
								tr("time_detail", DimensionWrapper.of(world).getIdentifierString(), WorldUtils.getWorldTime(world)),
								null
						),
						tr("message",
								Messenger.fancy(
										Messenger.s(nameOf(chunkTicketType), "d"),
										tr(
												"ticket_detail",
												chunkTicket.getTicketLevel(), expiryTicks > 0 ? Messenger.s(expiryTicks + " gt") : tr("permanent"),
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

	public static void onAddTicket(ServerLevel world, long position, Ticket chunkTicket)
	{
		if (TISAdditionLoggerRegistry.__ticket)
		{
			INSTANCE.onManipulateTicket(world, position, chunkTicket, ActionType.ADD);
		}
	}

	public static void onRemoveTicket(ServerLevel world, long position, Ticket chunkTicket)
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

		private BaseComponent getText(Translator translator)
		{
			return Messenger.formatting(translator.tr(this.translation), this.color);
		}
	}
}
