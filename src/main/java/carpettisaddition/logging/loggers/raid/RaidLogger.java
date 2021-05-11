package carpettisaddition.logging.loggers.raid;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.TextUtil;
import net.minecraft.village.raid.Raid;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;


public class RaidLogger extends AbstractLogger
{
	public static final String NAME = "raid";
	private static final RaidLogger instance = new RaidLogger();

	public RaidLogger()
	{
		super(NAME);
	}

	public static RaidLogger getInstance()
	{
		return instance;
	}

	public void onRaidCreated(Raid raid)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger(NAME).log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("created", "Raid created with id %d"), raid.getRaidId())),
					"g  @ ",
					TextUtil.getCoordinateText("w", raid.getCenter(), raid.getWorld().getRegistryKey())
			)};
		});
	}

	public void onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger(NAME).log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("invalidated", "Raid (id: %d) invalidated, reason: %s"), raid.getRaidId(), reason.tr()))
			)};
		});
	}

	public void onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger(NAME).log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("bad_omen_level_increased", "Raid (id: %d) increased its bad omen level to %d"), raid.getRaidId(), badOmenLevel))
			)};
		});
	}

	public void onCenterMoved(Raid raid, BlockPos pos)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		LoggerRegistry.getLogger(NAME).log(() -> {
			return new BaseText[]{Messenger.c(
					String.format("w %s", String.format(tr("center_moved", "Raid (id: %d) moves its center to"), raid.getRaidId())),
					"w  ",
					TextUtil.getCoordinateText("w", pos, raid.getWorld().getRegistryKey())
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

		private static final Translator TRANSLATOR = new Translator("raid_invalidate_reason");

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public String tr()
		{
			return TRANSLATOR.tr(getName(), getName().replace("_", " "));
		}
	}
}
