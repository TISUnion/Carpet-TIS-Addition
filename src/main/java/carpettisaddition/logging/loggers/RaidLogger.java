package carpettisaddition.logging.loggers;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.utils.Util;
import net.minecraft.entity.raid.Raid;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;


public class RaidLogger extends TranslatableLogger
{
	private static final RaidLogger instance = new RaidLogger();

	public RaidLogger()
	{
		super("raid");
	}

	public static RaidLogger getInstance()
	{
		return instance;
	}

	private void __onRaidCreated(Raid raid)
	{
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("created", "Raid created with id %d"), raid.getRaidId())),
					"g  @ ",
					Util.getCoordinateText("w", raid.getCenter(), raid.getWorld().getDimension())
			)};
		});
	}
	public static void onRaidCreated(Raid raid)
	{
		if (ExtensionLoggerRegistry.__raid)
		{
			instance.__onRaidCreated(raid);
		}
	}

	private void __onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("invalidated", "Raid (id: %d) invalidated, reason: %s"), raid.getRaidId(), reason.tr()))
			)};
		});
	}
	public static void onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		if (ExtensionLoggerRegistry.__raid)
		{
			instance.__onRaidInvalidated(raid, reason);
		}
	}

	private void __onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("bad_omen_level_increased", "Raid (id: %d) increased its bad omen level to %d"), raid.getRaidId(), badOmenLevel))
			)};
		});
	}
	public static void onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		if (ExtensionLoggerRegistry.__raid)
		{
			instance.__onBadOmenLevelIncreased(raid, badOmenLevel);
		}
	}

	private void __onCenterMoved(Raid raid, BlockPos pos)
	{
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("center_moved", "Raid (id: %d) moves its center to"), raid.getRaidId())),
					"w  ",
					Util.getCoordinateText("w", pos, raid.getWorld().getDimension())
			)};
		});
	}
	public static void onCenterMoved(Raid raid, BlockPos pos)
	{
		if (ExtensionLoggerRegistry.__raid)
		{
			instance.__onCenterMoved(raid, pos);
		}
	}

	public enum InvalidateReason
	{
		DIFFICULTY_PEACEFUL,
		GAMERULE_DISABLE,
		POI_REMOVED_BEFORE_SPAWN,
		TIME_OUT,
		RAIDER_CANNOT_SPAWN,
		RAID_VICTORY,
		RAID_DEFEAT;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public String tr()
		{
			return getName().replace("_", " ");
		}
	}
}
