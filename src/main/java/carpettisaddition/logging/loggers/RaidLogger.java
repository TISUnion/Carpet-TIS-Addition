package carpettisaddition.logging.loggers;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpet.utils.Translations;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.raid.Raid;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;


public class RaidLogger extends AbstractLogger
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

	public void onRaidCreated(Raid raid)
	{
		if (!ExtensionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("created", "Raid created with id %d"), raid.getRaidId())),
					"g  @ ",
					TextUtil.getCoordinateText("w", raid.getCenter(), raid.getWorld().getDimension())
			)};
		});
	}

	public void onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		if (!ExtensionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("invalidated", "Raid (id: %d) invalidated, reason: %s"), raid.getRaidId(), reason.tr()))
			)};
		});
	}

	public void onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		if (!ExtensionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("bad_omen_level_increased", "Raid (id: %d) increased its bad omen level to %d"), raid.getRaidId(), badOmenLevel))
			)};
		});
	}

	public void onCenterMoved(Raid raid, BlockPos pos)
	{
		if (!ExtensionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger("raid").log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("center_moved", "Raid (id: %d) moves its center to"), raid.getRaidId())),
					"w  ",
					TextUtil.getCoordinateText("w", pos, raid.getWorld().getDimension())
			)};
		});
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
			return Translations.tr("raid_invalidate_reason." + getName(), getName().replace("_", " "));
		}
	}
}
