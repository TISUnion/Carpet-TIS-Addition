/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CounterUtils;
import carpettisaddition.utils.GameUtils;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	public BaseText getTranslatedName()
	{
		return tr("name");
	}

	// Xxx Tracker
	public BaseText getTranslatedNameFull()
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

	protected static final int START_TRACKING_NO_OP = 0;
	protected static final int START_TRACKING_OK = 1;

	public int startTracking(@NotNull ServerCommandSource source, boolean isRestart, boolean showFeedback)
	{
		if (this.isTracking())
		{
			if (showFeedback)
			{
				Messenger.tell(source, Messenger.formatting(baseTranslator.tr("tracking_already_started", this.getTranslatedNameFull()), Formatting.RED));
			}
			return START_TRACKING_NO_OP;
		}
		this.tracking = true;
		this.startTick = GameUtils.getGameTime();
		this.startMillis = System.currentTimeMillis();
		if (showFeedback)
		{
			Messenger.tell(source, baseTranslator.tr("tracking_started", this.getTranslatedNameFull()), true);
		}
		this.initTracker();
		return START_TRACKING_OK;
	}

	protected static final int STOP_TRACKING_NO_OP = 0;
	protected static final int STOP_TRACKING_OK = 1;

	public int stopTracking(@Nullable ServerCommandSource source, boolean isRestart, boolean showFeedback)
	{
		boolean wasTracking = this.isTracking();
		if (source != null)
		{
			if (wasTracking)
			{
				this.reportTracking(source, false);
				if (showFeedback)
				{
					Messenger.tell(source, Messenger.s(" "));
					Messenger.tell(source, baseTranslator.tr("tracking_stopped", this.getTranslatedNameFull()), true);
				}
			}
			else if (showFeedback)
			{
				Messenger.tell(source, Messenger.s(" "));
				Messenger.tell(source, Messenger.formatting(baseTranslator.tr("tracking_not_started", this.getTranslatedNameFull()), Formatting.RED));
			}
		}
		this.tracking = false;
		return wasTracking ? STOP_TRACKING_OK : STOP_TRACKING_NO_OP;
	}

	public int restartTracking(ServerCommandSource source)
	{
		boolean wasTracking = this.isTracking();
		this.stopTracking(source, true, false);
		this.startTracking(source, true, false);
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
						executes(c -> this.startTracking(c.getSource(), false, true))
				).
				then(literal("stop")
						.executes(c -> this.stopTracking(c.getSource(), false, true))
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
		return CounterUtils.getTimeElapsed(this.getStartTick(), this.getStartMillis(), realtime);
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
		this.stopTracking(null, false, false);
	}

	/**
	 * Called when the tracker starts tracking
	 * Go initialize necessary statistics
	 */
	protected abstract void initTracker();

	/**
	 * Show tracking result to the command source
	 *
	 * @param realtime use real time or not. if not, use in-game time
	 */
	protected abstract void printTrackingResult(ServerCommandSource source, boolean realtime);
}
