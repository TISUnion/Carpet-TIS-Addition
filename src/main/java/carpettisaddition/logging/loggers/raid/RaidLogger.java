package carpettisaddition.logging.loggers.raid;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;


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
		this.log(() -> {
			return new MutableText[]{Messenger.c(
					tr("created", raid.getRaidId()),
					"g  @ ",
					Messenger.coord("w", raid.getCenter(), DimensionWrapper.of(raid.getWorld()))
			)};
		});
	}

	public void onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new MutableText[]{Messenger.c(
					tr("invalidated", raid.getRaidId(), reason.toText())
			)};
		});
	}

	public void onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new MutableText[]{Messenger.c(
					tr("bad_omen_level_increased", raid.getRaidId(), badOmenLevel)
			)};
		});
	}

	public void onCenterMoved(Raid raid, BlockPos pos)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new MutableText[]{Messenger.c(
					tr("center_moved", raid.getRaidId(), Messenger.coord(pos, DimensionWrapper.of(raid.getWorld())))
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

		private static final Translator TRANSLATOR = getInstance().getTranslator().getDerivedTranslator("raid_invalidate_reason");

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public MutableText toText()
		{
			return TRANSLATOR.tr(getName());
		}
	}
}
