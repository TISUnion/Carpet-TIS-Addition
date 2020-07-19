package carpettisaddition.logging.logHelpers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

import static java.lang.Math.max;

public class TicketLogHelper extends AbstractLogHelper
{
	private final String addedActionText;
	private final String removedActionText;
	public static TicketLogHelper inst = new TicketLogHelper();

	public TicketLogHelper()
	{
		super("ticket");
		addedActionText = "l " + tr("added");
		removedActionText = "r " + tr("removed");
	}

	private String formatSize(int range)
	{
		range = max(range, 0);
		int length = range * 2 - 1;
		return String.format("%d * %d", length, length);
	}

	private void onManipulateTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket, String actionText)
	{
		Logger logger = LoggerRegistry.getLogger("ticket");
		if (logger == null)
		{
			return;
		}
		logger.log((option) ->
		{
			if (Arrays.asList(option.split(",")).contains(chunkTicket.getType().toString()))
			{
				ChunkPos pos = new ChunkPos(position);
				BlockPos centerPos = pos.toBlockPos(8, 0, 8);
				long expiryTicks = chunkTicket.getType().getExpiryTicks();
				int level = chunkTicket.getLevel();
				String dimensionName = world.dimension.getType().toString();
				return new BaseText[]{Messenger.c(
						String.format("g [%s] ", world.getTime()),
						String.format(String.format("^w %s", tr("timeDetail", "World: %s\nGameTime: %d")), dimensionName, world.getTime()),
						String.format("w %s ", tr("Ticket")),
						String.format("d %s ", chunkTicket.getType()),
						String.format(String.format("^w %s", tr("ticketDetail", "Level = %d\nDuration = %s\nEntity processing chunks: %s\nLazy processing chunks: %s\nBorder chunks: %s")),
								chunkTicket.getLevel(), expiryTicks > 0 ? expiryTicks + " gt" : tr("Permanent"),
								formatSize(32 - level), formatSize(33 - level), formatSize(34 - level)
						),
						actionText + " ",
						String.format("g %s ", tr("at")),
						String.format("w [%d, %d]", pos.x, pos.z),
						String.format(String.format("^w %s", tr("teleportHint", "Click to teleport to chunk [%d, %d]")), pos.x, pos.z),
						String.format("?/execute in %s run tp %d ~ %d", dimensionName, centerPos.getX(), centerPos.getZ())
				)};
			}
			else
			{
				return null;
			}
		});
	}

	public void onAddTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
	{
		onManipulateTicket(world, position, chunkTicket, addedActionText);
	}

	public void onRemoveTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
	{
		onManipulateTicket(world, position, chunkTicket, removedActionText);
	}
}
