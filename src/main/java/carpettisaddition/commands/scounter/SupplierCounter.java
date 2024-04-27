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

package carpettisaddition.commands.scounter;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.GameUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.item.Item;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.DyeColor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SupplierCounter extends TranslationContext
{
	private final Object2LongMap<Item> counter = new Object2LongLinkedOpenHashMap<>();

	private final DyeColor color;
	private final BaseText prettyName;
	private long startTick;
	private long startMillis;
	private boolean running = false;

	public SupplierCounter(DyeColor color, Translator translator)
	{
		super(translator);
		this.color = color;
		this.counter.defaultReturnValue(0);
		this.prettyName = Messenger.fancy(
				Messenger.color(color),
				Messenger.s(color.getName()),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, this.getCommandBase())
		);
	}

	public BaseText getNameText()
	{
		return this.prettyName;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isRunning()
	{
		return this.running;
	}

	public void addItem(Item item, int count)
	{
		if (!this.isRunning())
		{
			this.start();
		}
		long newCount = this.counter.getLong(item) + count;
		this.counter.put(item, newCount);
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

	private String getCommandBase()
	{
		return String.format("/%s %s", SupplierCounterCommand.PREFIX, this.color.getName().toLowerCase());
	}

	private long getTotal()
	{
		return this.counter.values().
				//#if MC >= 11800
				longStream().
				//#else
				//$$ stream().mapToLong(x -> x).
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
				this.prettyName, "g : ",
				content,
				"g  (S)"
		);
	}

	public List<BaseText> report(boolean realTime)
	{
		if (!this.isRunning())
		{
			return Collections.singletonList(tr("not_started", this.prettyName));
		}

		long ticks = CounterUtil.getTimeElapsed(this.startTick, this.startMillis, realTime);
		long total = this.getTotal();

		BaseText realtimeSuffix = realTime ?
				Messenger.c("g (", Messenger.formatting(tr("realtime"), "g"), "g )") :
				Messenger.s("");

		List<BaseText> lines = Lists.newArrayList();
		lines.add(Messenger.c(
				tr("summary",
						this.prettyName,
						Messenger.c(String.format("w %.2f", CounterUtil.tickToMinute(ticks))),
						realtimeSuffix,
						total,
						Messenger.c(String.format("w %.2f", total / CounterUtil.tickToHour(ticks)))
				),
				"w  ",
				Messenger.fancy(
						"nb",
						Messenger.s("[X]"),
						tr("reset", this.prettyName),
						new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.getCommandBase() + " reset")
				)
		));
		this.counter.object2LongEntrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingLong(Object2LongMap.Entry::getLongValue))).
				forEach(entry -> {
					Item item = entry.getKey();
					long count = entry.getLongValue();
					lines.add(Messenger.c(
							"g - ",
							Messenger.item(item),
							"g : ",
							CounterUtil.ratePerHourText(count, ticks, "www")
					));
				});
		return lines;
	}
}
