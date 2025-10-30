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

package carpettisaddition.commands.speedtest;

import carpettisaddition.commands.speedtest.session.SpeedTestSessionMessenger;
import carpettisaddition.commands.speedtest.session.SpeedTestSessionMessengerImpl;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

import java.util.List;

public class SpeedTestReporter extends TranslationContext implements SpeedTestSessionMessenger
{
	private static final double BYTES_PER_MB = 1 << 20;
	private static final int BAR_WIDTH = 40;

	private final SpeedTestSessionMessenger messenger;
	private long lastReportNs = -1;

	public SpeedTestReporter(TestType testType, CommandSourceStack source)
	{
		super(SpeedTestCommand.getInstance().getTranslator().getDerivedTranslator("speed_test"));
		this.messenger = new SpeedTestSessionMessengerImpl(testType, source);
	}

	@Override
	public void sendMessage(BaseComponent message, boolean withCancelButton)
	{
		this.messenger.sendMessage(message, withCancelButton);
	}

	public void start(long totalBytes)
	{
		this.lastReportNs = System.nanoTime();
		this.sendMessage(tr(
				"start",
				Math.round(totalBytes / BYTES_PER_MB)
		), true);
	}

	/**
	 * Notes: might be on network thread
	 */
	public void onProgress(long timeCostNs, long processedBytes, long totalBytes)
	{
		long currentNs = System.nanoTime();

		// report every 1s
		if (currentNs - this.lastReportNs >= 1e9)
		{
			this.reportProgress(timeCostNs, processedBytes, totalBytes);
			this.lastReportNs = currentNs;
		}
	}

	/**
	 * Notes: might be on network thread
	 */
	public void reportProgress(long timeCostNs, long processedBytes, long totalBytes)
	{
		double percent = Mth.clamp(100.0 * processedBytes / totalBytes, 0.0, 100.0);

		List<Object> list = Lists.newArrayList();
		for (int i = 1; i <= BAR_WIDTH; i++)
		{
			list.add(percent >= i * 100.0 / BAR_WIDTH ? Messenger.s("#") : Messenger.s("-", ChatFormatting.DARK_GRAY));
		}
		BaseComponent bar = Messenger.c(list.toArray(new Object[0]));

		double timeCostSec = Math.max(1, timeCostNs) / 1e9;
		double mbps = processedBytes / timeCostSec / BYTES_PER_MB;

		this.sendMessage(tr(
				"progress",
				bar,
				String.valueOf(Math.round(percent)),
				String.format("%.2f", mbps)
		), true);
	}

	/**
	 * Notes: might be on network thread
	 */
	public void reportDone(long timeCostNs, long processedBytes, long totalBytes)
	{
		double timeCostSec = Math.max(1, timeCostNs) / 1e9;

		double mb = processedBytes / BYTES_PER_MB;
		double mbps = mb / timeCostSec;
		String mbStr = mb == (long)mb ? String.format("%d", (long)mb) : String.format("%.2f", mb);

		this.sendMessage(tr(
				"done",
				mbStr,
				String.format("%.2f", timeCostSec),
				String.format("%.2f", mbps)
		), false);
	}
}
