package carpettisaddition.commands;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.GameUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class AbstractTracker extends TranslationContext
{
	private static final Translator baseTranslator = new Translator("tracker");
	private final String name;
	private boolean tracking;
	private long startTick;
	private long startMillis;

	public AbstractTracker(String name)
	{
		super("tracker." + name.toLowerCase());
		this.name = name;
	}

	/*
	 * ---------------------
	 *  tracker name things
	 * ---------------------
	 */

	public String getName()
	{
		return this.name;
	}

	public String getCommandPrefix()
	{
		return this.name.toLowerCase();
	}

	// Xxx
	public MutableText getTranslatedName()
	{
		return tr("name");
	}

	// Xxx Tracker
	public MutableText getTranslatedNameFull()
	{
		return baseTranslator.tr("tracker_name_full", this.getTranslatedName());
	}

	/*
	 * -----------------------
	 *  status / info getters
	 * -----------------------
	 */

	public boolean isTracking()
	{
		return this.tracking;
	}

	public long getStartMillis()
	{
		return this.startMillis;
	}

	public long getStartTick()
	{
		return this.startTick;
	}

	/*
	 * ------------------------
	 *  for command executions
	 * ------------------------
	 */

	public int startTracking(ServerCommandSource source, boolean showFeedback)
	{
		if (this.isTracking())
		{
			if (showFeedback)
			{
				Messenger.tell(source, Messenger.formatting(baseTranslator.tr("tracking_already_started", this.getTranslatedNameFull()), Formatting.RED));
			}
			return 1;
		}
		this.tracking = true;
		this.startTick = GameUtil.getGameTime();
		this.startMillis = System.currentTimeMillis();
		if (showFeedback)
		{
			Messenger.tell(source, baseTranslator.tr("tracking_started", this.getTranslatedNameFull()), true);
		}
		this.initTracker();
		return 1;
	}

	public int stopTracking(ServerCommandSource source, boolean showFeedback)
	{
		if (source != null)
		{
			if (this.isTracking())
			{
				this.reportTracking(source, false);
				if (showFeedback)
				{
					Messenger.tell(source, Messenger.c(Messenger.newLine(), baseTranslator.tr("tracking_stopped", this.getTranslatedNameFull())), true);
				}
			}
			else if (showFeedback)
			{
				Messenger.tell(source, Messenger.c(Messenger.newLine(), Messenger.formatting(baseTranslator.tr("tracking_not_started", this.getTranslatedNameFull()), Formatting.RED)));
			}
		}
		this.tracking = false;
		return 1;
	}

	public int restartTracking(ServerCommandSource source)
	{
		boolean wasTracking = this.isTracking();
		this.stopTracking(source, false);
		this.startTracking(source, false);
		if (wasTracking)
		{
			Messenger.tell(source, Messenger.s(" "));
		}
		Messenger.tell(source, baseTranslator.tr("tracking_restarted", this.getTranslatedNameFull()), true);
		return 1;
	}

	protected int doWhenTracking(ServerCommandSource source, Runnable runnable)
	{
		if (this.isTracking())
		{
			runnable.run();
		}
		else
		{
			Messenger.tell(source, Messenger.formatting(baseTranslator.tr("tracking_not_started", this.getTranslatedNameFull()), Formatting.RED));
		}
		return 1;
	}

	public int reportTracking(ServerCommandSource source, boolean realtime)
	{
		return this.doWhenTracking(source, () -> this.printTrackingResult(source, realtime));
	}

	public LiteralArgumentBuilder<ServerCommandSource> getTrackingArgumentBuilder()
	{
		return literal("tracking").
				executes(c -> this.reportTracking(c.getSource(), false)).
				then(literal("start").
						executes(c -> this.startTracking(c.getSource(), true))
				).
				then(literal("stop")
						.executes(c -> this.stopTracking(c.getSource(), true))
				).
				then(literal("restart").
						executes(c -> this.restartTracking(c.getSource()))
				).
				then(literal("realtime").
						executes(c -> this.reportTracking(c.getSource(), true))
				);
	}

	/*
	 * -------
	 *  Utils
	 * -------
	 */

	protected long getTrackedTick(boolean realtime)
	{
		return Math.max(1, realtime ? (System.currentTimeMillis() - this.getStartMillis()) / 50 : GameUtil.getGameTime() - this.getStartTick());
	}

	// send general header for tracking report and return the processed "ticks"
	protected long sendTrackedTime(ServerCommandSource source, boolean realtime)
	{
		long ticks = this.getTrackedTick(realtime);
		Messenger.tell(
				source,
				Messenger.c(
						"w  \n",
						"g ----------- ", this.getTranslatedNameFull(), "g  -----------\n",
						baseTranslator.tr("tracked", Messenger.c(
								String.format("w %.2f min (", (double)ticks / (20 * 60)),
								baseTranslator.tr(realtime ? "real_time" : "in_game"),
								"w )"
						))
				)
		);
		return ticks;
	}

	/*
	 * ------------
	 *  Interfaces
	 * ------------
	 */

	/**
	 * Stop tracking, call this when server stops
	 * e.g. inside {@link carpettisaddition.CarpetTISAdditionServer#onServerClosed}
	 */
	public void stop()
	{
		this.stopTracking(null, false);
	}

	/**
	 * Called when the tracker starts tracking
	 * Go initialize necessary statistics
	 */
	protected abstract void initTracker();

	/**
	 * Show tracking result to the command source
	 * @param realtime use real time or not. if not, use in-game time
	 */
	protected abstract void printTrackingResult(ServerCommandSource source, boolean realtime);
}
