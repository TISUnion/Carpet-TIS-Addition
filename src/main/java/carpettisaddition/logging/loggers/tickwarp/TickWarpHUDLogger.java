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

package carpettisaddition.logging.loggers.tickwarp;

import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class TickWarpHUDLogger extends AbstractHUDLogger implements CommandExtender
{
	public static final String NAME = "tickWarp";
	private static final TickWarpHUDLogger INSTANCE = new TickWarpHUDLogger();

	private TickWarpInfo info = new TickWarpInfo();
	private final MemorizedTickWarpInfo historyInfo = new MemorizedTickWarpInfo();

	private TickWarpHUDLogger()
	{
		super(NAME, true);
	}

	public static TickWarpHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return new String[]{"bar", "value"};
	}

	private long getTotalTicks()
	{
		return this.info.getTotalTicks();
	}

	private long getRemainingTicks()
	{
		return this.info.getRemainingTicks();
	}

	private long getCompletedTicks()
	{
		return this.getTotalTicks() - this.getRemainingTicks();
	}

	private long getCurrentTime()
	{
		return this.info.getCurrentTime();
	}

	private long getStartTime()
	{
		return this.info.getStartTime();
	}

	private ServerPlayerEntity getTimeAdvancer()
	{
		return this.info.getTimeAdvancer();
	}

	private double getAverageMSPT()
	{
		double milliSeconds = Math.max(this.getCurrentTime() - this.getStartTime(), 1) / 1e6;
		return milliSeconds / this.getCompletedTicks();
	}

	private boolean isWarping()
	{
		return this.info.isWarping();
	}

	private double getAverageTPS()
	{
		double secondPerTick = this.getAverageMSPT() / 1e3;
		return 1.0 / secondPerTick;
	}

	private BaseText getSourceName()
	{
		ServerPlayerEntity advancer = this.getTimeAdvancer();
		return advancer != null ? (BaseText)advancer.getName() : tr("server");
	}

	private double getProgressRate()
	{
		return (double)this.getCompletedTicks() / Math.max(this.getTotalTicks(), 1);
	}

	private BaseText getProgressBar()
	{
		double progressRate = this.getProgressRate();
		List<Object> list = Lists.newArrayList();
		list.add("g [");
		for (int i = 1; i <= 10; i++)
		{
			list.add(progressRate >= i / 10.0D ? "g #" : "f -");
		}
		list.add("g ]");
		return Messenger.c(list.toArray(new Object[0]));
	}

	private BaseText getDurationRatio()
	{
		return Messenger.c(
				String.format("g %d", this.getCompletedTicks()),
				"f /",
				String.format("g %d", this.getTotalTicks())
		);
	}

	private BaseText getProgressPercentage()
	{
		return Messenger.c(String.format("g %.1f%%", this.getProgressRate() * 100));
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		if (!this.isWarping())
		{
			return null;
		}
		List<Object> list = Lists.newArrayList();
		list.add("g Warp ");
		if (option.equals("bar"))  // progress bar
		{
			list.add(this.getProgressBar());
		}
		else if (option.equals("value"))  // regular value
		{
			list.add(this.getDurationRatio());
		}
		else  // fallback
		{
			list.add(this.getProgressBar());
		}
		list.add("w  ");
		list.add(this.getProgressPercentage());
		return new BaseText[]{Messenger.c(list.toArray(new Object[0]))};
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.then(
				literal("warp").then(
						literal("status").
						executes(c -> this.showTickWarpInfo(c.getSource()))
				)
		);
	}

	private void addLine(List<BaseText> list, BaseText info, Object data)
	{
		list.add(Messenger.c(info, "g : ", data));
	}

	private BaseText getTimeInfo(long ticks)
	{
		return tr(
				"time_info",
				String.format("%.2f", ticks / 20.0D / 60.0D),
				String.format("%.2f", ticks / this.getAverageTPS() / 60.0D)
		);
	}

	private synchronized void generateTickWarpInfo(List<BaseText> result, TickWarpInfo specifiedInfo)
	{
		TickWarpInfo infoBackup = this.info;
		try
		{
			this.info = specifiedInfo;
			result.add(Messenger.s(" "));
			this.addLine(result, tr("starter"), this.getSourceName());
			this.addLine(result, tr("average_tps"), String.format("w %.2f", this.getAverageTPS()));
			this.addLine(result, tr("average_mspt"), String.format("w %.2f", this.getAverageMSPT()));
			this.addLine(result, tr("elapsed_time"), this.getTimeInfo(this.getCompletedTicks()));
			this.addLine(result, tr("estimated_time"), this.getTimeInfo(this.getRemainingTicks()));
			result.add(Messenger.c(
					this.getProgressBar(),
					"w  ",
					this.getProgressPercentage(),
					"w  ",
					this.getDurationRatio()
			));
		}
		finally
		{
			this.info = infoBackup;
		}
	}

	private int showTickWarpInfo(ServerCommandSource source)
	{
		List<BaseText> result = Lists.newArrayList();
		if (this.isWarping())
		{
			this.generateTickWarpInfo(result, this.info);
		}
		else if (this.historyInfo.hasData())
		{
			result.add(tr("show_history_header", String.format("%.2f", (System.nanoTime() - this.historyInfo.getLastRecordingTime()) / 1e9 / 60.0D)));
			this.generateTickWarpInfo(result, this.historyInfo);
		}
		else
		{
			result.add(tr("not_started"));
		}
		Messenger.tell(source, result);
		return 1;
	}

	public void recordTickWarpResult()
	{
		this.historyInfo.recordResultIfsuitable();
	}

	public void recordTickWarpAdvancer()
	{
		this.historyInfo.recordTickWarpAdvancer();
	}
}
