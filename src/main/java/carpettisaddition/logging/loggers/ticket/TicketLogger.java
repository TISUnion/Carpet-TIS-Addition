package carpettisaddition.logging.loggers.ticket;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
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

public class TicketLogger extends AbstractLogger
{
	public static final String NAME = "ticket";
	private static final TicketLogger INSTANCE = new TicketLogger();

	private final List<ChunkTicketType<?>> tickTypes = Lists.newArrayList();

	public TicketLogger()
	{
		super(NAME);
	}

	public static TicketLogger getInstance()
	{
		return INSTANCE;
	}

	public void addTicketType(ChunkTicketType<?> ticketType)
	{
		this.tickTypes.add(ticketType);
	}

	private String[] getLoggingSuggestions()
	{
		List<String> suggestions = this.tickTypes.stream().map(ChunkTicketType::toString).collect(Collectors.toList());
		suggestions.add(ChunkTicketType.PORTAL + "," + ChunkTicketType.PLAYER);
		suggestions.add(ChunkTicketType.PORTAL + "," + ChunkTicketType.DRAGON);
		return suggestions.toArray(new String[0]);
	}

	public Logger getStandardLogger()
	{
		return new Logger(TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, ChunkTicketType.PORTAL.toString(), null){
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

	private void onManipulateTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket, ActionType actionType)
	{
		LoggerRegistry.getLogger(NAME).log((option) ->
		{
			if (Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(chunkTicket.getType().toString()))
			{
				ChunkPos pos = new ChunkPos(position);
				long expiryTicks = chunkTicket.getType().getExpiryTicks();
				int level = chunkTicket.getLevel();
				return new BaseText[]{Messenger.c(
						Messenger.fancy(
								Messenger.s(String.format("[%s] ", world.getTime()), "g"),
								tr("time_detail", DimensionWrapper.of(world).getIdentifierString(), world.getTime()),
								null
						),
						tr("message",
								Messenger.fancy(
										Messenger.s(chunkTicket.getType().toString(), "d"),
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

	public static void onAddTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
	{
		if (TISAdditionLoggerRegistry.__ticket)
		{
			INSTANCE.onManipulateTicket(world, position, chunkTicket, ActionType.ADD);
		}
	}

	public static void onRemoveTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
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
