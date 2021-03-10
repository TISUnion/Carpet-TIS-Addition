package carpettisaddition.logging.loggers.ticket;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.stacktrace.StackTracePrinter;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

import static java.lang.Math.max;

public class TicketLogger extends AbstractLogger
{
	public static final String NAME = "ticket";
	private static final TicketLogger INSTANCE = new TicketLogger();

	public TicketLogger()
	{
		super(NAME);
	}

	public static TicketLogger getInstance()
	{
		return INSTANCE;
	}

	private String getAddedActionText()
	{
		return "l " + this.tr(" added");
	}

	private String getRemovedActionText()
	{
		return "r " + this.tr(" removed");
	}

	private String formatSize(int range)
	{
		range = max(range, 0);
		int length = range * 2 - 1;
		return String.format("%d * %d", length, length);
	}

	private void onManipulateTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket, String actionText)
	{
		LoggerRegistry.getLogger(NAME).log((option) ->
		{
			if (Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(chunkTicket.getType().toString()))
			{
				ChunkPos pos = new ChunkPos(position);
				BlockPos centerPos = pos.toBlockPos(8, 0, 8);
				long expiryTicks = chunkTicket.getType().method_20629();
				int level = chunkTicket.getLevel();
				String dimensionName = world.dimension.getType().toString();
				return new BaseText[]{Messenger.c(
						String.format("g [%s] ", world.getTime()),
						String.format(String.format("^w %s", tr("time_detail", "World: %s\nGameTime: %d")), dimensionName, world.getTime()),
						String.format("w %s", tr("Ticket ")),
						String.format("d %s", chunkTicket.getType()),
						String.format(String.format("^w %s", tr("ticket_detail", "Level = %d\nDuration = %s\nEntity processing chunks: %s\nLazy processing chunks: %s\nBorder chunks: %s")),
								chunkTicket.getLevel(), expiryTicks > 0 ? expiryTicks + " gt" : tr("Permanent"),
								formatSize(32 - level), formatSize(33 - level), formatSize(34 - level)
						),
						actionText,
						String.format("g %s", tr(" at")),
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
			INSTANCE.onManipulateTicket(world, position, chunkTicket, INSTANCE.getAddedActionText());
		}
	}

	public static void onRemoveTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
	{
		if (TISAdditionLoggerRegistry.__ticket)
		{
			INSTANCE.onManipulateTicket(world, position, chunkTicket, INSTANCE.getRemovedActionText());
		}
	}
}
