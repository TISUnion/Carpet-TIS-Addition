package carpettisaddition.logging.loggers.phantom;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.StringUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhantomLogger extends AbstractLogger
{
	public static final String NAME = "phantom";
	private static final PhantomLogger INSTANCE = new PhantomLogger();
	private static final int PHANTOM_SPAWNING_TIME = 72000;
	private static final List<Integer> REMINDER_TICKS = Lists.newArrayList(PHANTOM_SPAWNING_TIME * 3 / 4, PHANTOM_SPAWNING_TIME);

	private PhantomLogger()
	{
		super(NAME, false);
	}

	public static PhantomLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String getDefaultLoggingOption()
	{
		return LoggingOption.DEFAULT.getName();
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return LoggingOption.getSuggestions();
	}

	private MutableText pack(MutableText message)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", tr("header"), "g ] "),
						Messenger.s(command),
						new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
				),
				message
		);
	}

	public void onPhantomSpawn(PlayerEntity spawnerPlayer, int phantomAmount)
	{
		if (!TISAdditionLoggerRegistry.__phantom)
		{
			return;
		}
		this.log(option -> {
			if (LoggingOption.SPAWNING.isContainedIn(option))
			{
				return new MutableText[]{
						pack(tr("summon", Messenger.entity("b", spawnerPlayer), phantomAmount, EntityType.PHANTOM.getName()))
				};
			}
			return null;
		});
	}

	public void tick()
	{
		if (!TISAdditionLoggerRegistry.__phantom)
		{
			return;
		}

		this.log((option, player) -> {
			if (LoggingOption.REMINDER.isContainedIn(option))
			{
				ServerStatHandler serverStatHandler = ((ServerPlayerEntity)player).getStatHandler();
				int timeSinceRest = MathHelper.clamp(serverStatHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
				if (REMINDER_TICKS.contains(timeSinceRest))
				{
					int timeUntilSpawn = PHANTOM_SPAWNING_TIME - timeSinceRest;
					String sinceRest = StringUtil.fractionDigit(CounterUtil.tickToMinute(timeSinceRest), 0);
					String untilSpawn = StringUtil.fractionDigit(CounterUtil.tickToMinute(timeUntilSpawn), 0);
					return new MutableText[]{
							pack(tr("reminder.time_since_rest", sinceRest)),
							pack(tr(timeUntilSpawn != 0 ? "reminder.regular" : "reminder.now", EntityType.PHANTOM.getName(), untilSpawn))
					};
				}
			}
			return null;
		});
	}

	public enum LoggingOption
	{
		SPAWNING,
		REMINDER;

		public static final LoggingOption DEFAULT = SPAWNING;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public static String[] getSuggestions()
		{
			List<String> suggestions = Lists.newArrayList();
			suggestions.addAll(Arrays.stream(values()).map(LoggingOption::getName).collect(Collectors.toList()));
			suggestions.add(createCompoundOption(SPAWNING.getName(), REMINDER.getName()));
			return suggestions.toArray(new String[0]);
		}

		public boolean isContainedIn(String option)
		{
			return Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}
	}
}
