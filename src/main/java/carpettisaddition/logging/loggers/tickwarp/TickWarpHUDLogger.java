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
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;

import java.util.List;

import static net.minecraft.commands.Commands.literal;

//#if MC >= 12003
//$$ import carpettisaddition.helpers.rule.tickCommandCarpetfied.TickCommandCarpetfiedRules;
//#endif

public class TickWarpHUDLogger extends AbstractHUDLogger implements CommandExtender
{
	public static final String NAME = "tickWarp";
	private static final TickWarpHUDLogger INSTANCE = new TickWarpHUDLogger();

	private final TickWarpInfo realtimeInfo = new DefaultTickWarpInfo();
	private final MemorizedTickWarpInfo historyInfo = new MemorizedTickWarpInfo(this.realtimeInfo);

	private TickWarpHUDLogger()
	{
		super(NAME, true);
	}

	public static TickWarpHUDLogger getInstance()
	{
		return INSTANCE;
	}

	// ----------------------- TickWarpInfo processors -----------------------

	private BaseComponent getSourceName(TickWarpInfo info)
	{
		CommandSourceStack advancer = info.getTimeAdvancer();
		return advancer != null ? (BaseComponent)advancer.getDisplayName() : tr("server");
	}

	private BaseComponent getProgressBar(TickWarpInfoReader info)
	{
		double progressRate = info.getProgressRate();
		List<Object> list = Lists.newArrayList();
		list.add("g [");
		for (int i = 1; i <= 10; i++)
		{
			list.add(progressRate >= i / 10.0D ? "g #" : "f -");
		}
		list.add("g ]");
		return Messenger.c(list.toArray(new Object[0]));
	}

	private BaseComponent getDurationRatio(TickWarpInfoReader info)
	{
		return Messenger.c(
				String.format("g %d", info.getCompletedTicks()),
				"f /",
				String.format("g %d", info.getTotalTicks())
		);
	}

	private BaseComponent getProgressPercentage(TickWarpInfoReader info)
	{
		return Messenger.c(String.format("g %.1f%%", info.getProgressRate() * 100));
	}

	// ----------------------- As a HUD logger -----------------------

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return new String[]{"bar", "value"};
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		if (!this.realtimeInfo.isWarping())
		{
			return null;
		}
		List<Object> list = Lists.newArrayList();
		list.add("g Warp ");
		if (option.equals("bar"))  // progress bar
		{
			list.add(this.getProgressBar(this.realtimeInfo));
		}
		else if (option.equals("value"))  // regular value
		{
			list.add(this.getDurationRatio(this.realtimeInfo));
		}
		else  // fallback
		{
			list.add(this.getProgressBar(this.realtimeInfo));
		}
		list.add("w  ");
		list.add(this.getProgressPercentage(this.realtimeInfo));
		return new BaseComponent[]{Messenger.c(list.toArray(new Object[0]))};
	}

	// ----------------------- As a command extension -----------------------

	/**
	 * Hook at
	 * - "tick" node in mc < 1.20.3
	 * - "sprint" node in mc >= 1.20.3
	 */
	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		//#if MC >= 12003
		//$$ var warpNode = context.node;
		//#else
		LiteralArgumentBuilder<CommandSourceStack> warpNode = literal("warp");
		//#endif

		warpNode.then(literal("status").
				//#if MC >= 12003
				//$$ requires(s -> TickCommandCarpetfiedRules.tickCommandEnhance()).
				//#endif
				executes(c -> this.showTickWarpInfo(c.getSource()))
		);

		//#if MC < 12003
		context.node.then(warpNode);
		//#endif
	}

	private static void addLine(List<BaseComponent> list, BaseComponent name, Object data)
	{
		list.add(Messenger.c(name, "g : ", data));
	}

	private BaseComponent getTimeInfo(TickWarpInfoReader info, long ticks)
	{
		return tr(
				"time_info",
				String.format("%.2f", ticks / 20.0D / 60.0D),
				String.format("%.2f", ticks / info.getAverageTPS() / 60.0D)
		);
	}

	private void generateTickWarpInfo(TickWarpInfo info, List<BaseComponent> result)
	{
		result.add(Messenger.s(" "));
		addLine(result, tr("starter"), this.getSourceName(info));
		addLine(result, tr("average_tps"), String.format("w %.2f", info.getAverageTPS()));
		addLine(result, tr("average_mspt"), String.format("w %.2f", info.getAverageMSPT()));
		addLine(result, tr("elapsed_time"), this.getTimeInfo(info, info.getCompletedTicks()));
		addLine(result, tr("estimated_time"), this.getTimeInfo(info, info.getRemainingTicks()));
		result.add(Messenger.c(
				this.getProgressBar(info),
				"w  ",
				this.getProgressPercentage(info),
				"w  ",
				this.getDurationRatio(info)
		));
	}

	private int showTickWarpInfo(CommandSourceStack source)
	{
		List<BaseComponent> result = Lists.newArrayList();
		if (this.realtimeInfo.isWarping())
		{
			this.generateTickWarpInfo(this.realtimeInfo, result);
		}
		else if (this.historyInfo.hasData())
		{
			result.add(tr("show_history_header", String.format("%.2f", (System.nanoTime() - this.historyInfo.getLastRecordingTime()) / 1e9 / 60.0D)));
			this.generateTickWarpInfo(this.historyInfo, result);
		}
		else
		{
			result.add(tr("not_started"));
		}
		Messenger.tell(source, result);
		return 1;
	}

	// ----------------------- Hooks -----------------------

	public void recordTickWarpAdvancer(CommandSourceStack advancer)
	{
		this.realtimeInfo.setTimeAdvancer(advancer);
		this.historyInfo.setTimeAdvancer(advancer);
	}

	public void recordTickWarpResult()
	{
		this.historyInfo.recordResultIfsuitable();
	}
}
