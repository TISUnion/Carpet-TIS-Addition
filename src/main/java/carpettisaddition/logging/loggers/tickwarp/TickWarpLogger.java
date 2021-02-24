package carpettisaddition.logging.loggers.tickwarp;

import carpet.helpers.TickSpeed;
import carpet.utils.Messenger;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class TickWarpLogger extends AbstractHUDLogger implements CommandExtender
{
	public static final String NAME = "tickWarp";

	private static final TickWarpLogger INSTANCE = new TickWarpLogger();

	private TickWarpLogger()
	{
		super(NAME);
	}

	public static TickWarpLogger getInstance()
	{
		return INSTANCE;
	}

	private long getTotalTicks()
	{
		return TickSpeed.time_warp_scheduled_ticks;
	}

	private long getRemainingTicks()
	{
		return TickSpeed.time_bias;
	}

	private long getCompletedTicks()
	{
		return this.getTotalTicks() - this.getRemainingTicks();
	}

	private double getAverageTPS()
	{
		double seconds = Math.max(System.nanoTime() - TickSpeed.time_warp_start_time, 1) / 1E9D;
		return this.getCompletedTicks() / seconds;
	}

	private BaseText getSourceName()
	{
		return TickSpeed.time_advancerer != null ? (BaseText)TickSpeed.time_advancerer.getName() : Messenger.s(this.tr("Server"));
	}

	private boolean isWarping()
	{
		return TickSpeed.time_bias > 0;
	}

	private double getProgressRate()
	{
		return this.isWarping() ? (double)this.getCompletedTicks() / Math.max(this.getTotalTicks(), 1) : 0.0D;
	}

	private BaseText getProgressBar()
	{
		List<Object> list = Lists.newArrayList();
		list.add("g [");
		for (int i = 1; i <= 10; i++)
		{
			list.add(this.getProgressRate() >= i / 10.0D ? "g #" : "f -");
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
		return this.isWarping() ? Messenger.c(String.format("g %.1f%%", this.getProgressRate() * 100)) : Messenger.s("N/A", "g");
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
		list.add("w  ");
		list.add(this.getProgressPercentage());
		return new BaseText[]{Messenger.c(list.toArray(new Object[0]))};
	}

	public void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder.then(
				literal("warp").then(
						literal("status").
						executes(c -> this.showTickWarpInfo(c.getSource()))
				)
		);
	}

	private int showTickWarpInfo(ServerCommandSource source)
	{
		List<BaseText> result = Lists.newArrayList();
		if (this.isWarping())
		{
			result.add(Messenger.s(" "));
			result.add(Messenger.c(
					String.format("w %s", this.tr("Starter")),
					"g : ",
					this.getSourceName()
			));
			result.add(Messenger.c(
					String.format("w %s", this.tr("Average TPS")),
					"g : ",
					String.format("w %.2f", this.getAverageTPS())
			));
			result.add(Messenger.c(
					String.format("w %s", this.tr("Estimated remaining time")),
					"g : ",
					String.format("w %.2fmin", this.getRemainingTicks() / this.getAverageTPS() / 60)
			));
			result.add(Messenger.c(
					this.getProgressBar(),
					"w  ",
					this.getProgressPercentage(),
					"w  ",
					this.getDurationRatio()
			));
		}
		else
		{
			result.add(Messenger.s(this.tr("not_started", "Tick warp has not started")));
		}
		Messenger.send(source, result);
		return 1;
	}
}
