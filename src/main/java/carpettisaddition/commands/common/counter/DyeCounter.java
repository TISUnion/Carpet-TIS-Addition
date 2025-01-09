/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.common.counter;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.GameUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.DyeColor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class DyeCounter<Key extends DyeCounterKey> extends TranslationContext
{
	private final Object2LongMap<Key> counter = new Object2LongLinkedOpenHashMap<>();

	private final DyeColor color;
	private final BaseText colorText;
	private final String commandPrefix;
	private final Translator baseTr;
	private long startTick;
	private long startMillis;
	private boolean running = false;

	// translator base key is usually like "carpettisaddition.command.supplier_counter"
	public DyeCounter(DyeColor color, Translator translator, String commandPrefix)
	{
		super(translator);
		this.baseTr = new Translator("command.dye_counter.counter");
		this.color = color;
		this.commandPrefix = commandPrefix;
		this.counter.defaultReturnValue(0);
		this.colorText = Messenger.fancy(
				Messenger.colored(Messenger.color(color), color),
				Messenger.s(color.getName()),
				Messenger.ClickEvents.suggestCommand(this.getSelfCommandBase())
		);
	}

	protected abstract Comparator<? super Object2LongMap.Entry<Key>> getReportOrderComparator();

	protected abstract BaseText getSymbolText();

	public BaseText getColorText()
	{
		return this.colorText;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isRunning()
	{
		return this.running;
	}

	public void addForKey(Key key, long count)
	{
		if (!this.isRunning())
		{
			this.start();
		}
		long newCount = this.counter.getLong(key) + count;
		this.counter.put(key, newCount);
	}

	private void start()
	{
		this.startTick = GameUtil.getGameTime();
		this.startMillis = System.currentTimeMillis();
		this.counter.clear();
		this.running = true;
	}

	public void reset()
	{
		this.running = false;
	}

	private String getSelfCommandBase()
	{
		return String.format("/%s %s", this.commandPrefix, this.color.getName().toLowerCase());
	}

	private long getTotal()
	{
		return this.counter.values().
				//#if MC >= 11800
				//$$ longStream().
				//#else
				stream().mapToLong(x -> x).
				//#endif
				sum();
	}

	public BaseText reportBrief(boolean realTime)
	{
		BaseText content;
		if (this.isRunning())
		{
			long ticks = CounterUtil.getTimeElapsed(this.startTick, this.startMillis, realTime);
			long total = this.getTotal();
			content = Messenger.s(String.format("%d, %.1f/h, %.1f min", total, total / CounterUtil.tickToHour(ticks), CounterUtil.tickToMinute(ticks)));
		}
		else
		{
			content = Messenger.s("N/A", "g");
		}
		return Messenger.c(
				this.colorText, "g : ",
				content,
				"g  (",
				this.getSymbolText(),
				"g )"
		);
	}

	public List<BaseText> report(boolean realTime)
	{
		BaseText counterNameText = Messenger.hover(tr("counter_name"), Messenger.s(this.commandPrefix));
		if (!this.isRunning())
		{
			return Collections.singletonList(this.baseTr.tr("not_started", this.colorText, counterNameText));
		}

		long ticks = CounterUtil.getTimeElapsed(this.startTick, this.startMillis, realTime);
		long total = this.getTotal();

		BaseText realtimeSuffix = realTime ?
				Messenger.c("g (", Messenger.formatting(this.baseTr.tr("realtime"), "g"), "g )") :
				Messenger.s("");

		List<BaseText> lines = Lists.newArrayList();
		lines.add(Messenger.c(
				this.baseTr.tr("summary",
						tr("key_name_pc"),
						this.colorText,
						counterNameText,
						Messenger.c(String.format("w %.2f", CounterUtil.tickToMinute(ticks))),
						realtimeSuffix,
						total,
						Messenger.c(String.format("w %.2f", total / CounterUtil.tickToHour(ticks)))
				),
				"w  ",
				Messenger.fancy(
						"nb",
						Messenger.s("[X]"),
						this.baseTr.tr("reset", this.colorText),
						Messenger.ClickEvents.runCommand(this.getSelfCommandBase() + " reset")
				)
		));
		this.counter.object2LongEntrySet().stream().
				sorted(this.getReportOrderComparator()).
				forEach(entry -> {
					Key key = entry.getKey();
					long count = entry.getLongValue();
					lines.add(Messenger.c(
							"g - ",
							key.getText(),
							"g : ",
							CounterUtil.ratePerHourText(count, ticks, "www")
					));
				});
		return lines;
	}
}
