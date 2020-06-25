package carpettisaddition.logging.logHelpers;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpet.utils.Translations;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

import static java.lang.Math.max;

public class ticketLoggerHelper
{
	public static String tr(String key, String text)
	{
		return Translations.tr("logger.ticketLogger." + key, text);
	}

	private static String formatSize(int range)
	{
		range = max(range, 0);
		int length = range * 2 - 1;
		return String.format("%d * %d", length, length);
	}

	private static void onManipulateTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket, String actionText)
	{
		LoggerRegistry.getLogger("ticket").log((option) ->
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
						String.format("w %s ", tr("ticket", "Ticket")),
						String.format("d %s ", chunkTicket.getType()),
						String.format(String.format("^w %s", tr("ticketDetail", "Level = %d\nDuration = %s\nEntity processing chunks: %s\nLazy processing chunks: %s\nBorder chunks: %s")),
								chunkTicket.getLevel(), expiryTicks > 0 ? expiryTicks + " gt" : tr("permanent", "Permanent"),
								formatSize(32 - level), formatSize(33 - level), formatSize(34 - level)
						),
						actionText + " ",
						String.format("g %s ", tr("at", "at")),
						String.format("w [%d, %d]", pos.x, pos.z),
						String.format(String.format("^w %s", tr("teleportHit", "Click to teleport to chunk [%d, %d]")), pos.x, pos.z),
						String.format("?/execute in %s run tp %d ~ %d", dimensionName, centerPos.getX(), centerPos.getZ())
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
		onManipulateTicket(world, position, chunkTicket, "l " + tr("add", "added"));
	}

	public static void onRemoveTicket(ServerWorld world, long position, ChunkTicket<?> chunkTicket)
	{
		onManipulateTicket(world, position, chunkTicket, "r " + tr("remove", "removed"));
	}
}
