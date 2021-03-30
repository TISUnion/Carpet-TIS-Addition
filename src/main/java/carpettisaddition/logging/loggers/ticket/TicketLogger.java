package carpettisaddition.logging.loggers.ticket;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.stacktrace.StackTracePrinter;
import com.google.common.collect.Lists;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
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
				BlockPos centerPos = pos.getStartPos().add(8, 0, 8);
				long expiryTicks = chunkTicket.getType().getExpiryTicks();
				int level = chunkTicket.getLevel();
				String dimensionName = world.getRegistryKey().getValue().toString();
				return new BaseText[]{Messenger.c(
						String.format("g [%s] ", world.getTime()),
						String.format(String.format("^w %s", tr("time_detail", "World: %s\nGameTime: %d")), dimensionName, world.getTime()),
						String.format("w %s", tr("Ticket ")),
						String.format("d %s", chunkTicket.getType()),
						String.format(String.format("^w %s", tr("ticket_detail", "Level = %d\nDuration = %s\nEntity processing chunks: %s\nLazy processing chunks: %s\nBorder chunks: %s")),
								chunkTicket.getLevel(), expiryTicks > 0 ? expiryTicks + " gt" : tr("Permanent"),
								formatSize(32 - level), formatSize(33 - level), formatSize(34 - level)
						),
						TextUtil.getSpaceText(),
						actionType.getText(this.getTranslator()),
						TextUtil.getSpaceText(),
						String.format("g %s", tr("at")),
						String.format("w  [%d, %d]", pos.x, pos.z),
						String.format(String.format("^w %s", tr("teleport_hint", "Click to teleport to chunk [%d, %d]")), pos.x, pos.z),
						String.format("?/execute in %s run tp %d ~ %d", dimensionName, centerPos.getX(), centerPos.getZ()),
						"w  ",
						StackTracePrinter.create().ignore(TicketLogger.class).deobfuscate().toSymbolText()
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
			return Messenger.s(translator.tr(this.translation), this.color);
		}
	}
}
