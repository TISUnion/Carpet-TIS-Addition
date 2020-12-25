package carpettisaddition.logging.loggers.entity;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.stacktrace.StackTracePrinter;
import com.google.common.base.Joiner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public abstract class EntityLogger<T extends Entity> extends AbstractLogger
{
	protected final String loggerName;

	private static final EntityLogger<Entity> translator = new EntityLogger<Entity>("entity"){};

	public EntityLogger(String loggerName)
	{
		super(loggerName);
		this.loggerName = loggerName;
	}

	protected BaseText getNameText(T entity)
	{
		return TextUtil.getTranslatedName(entity.getType().getTranslationKey());
	}

	protected BaseText getNameTextHoverText(T entity)
	{
		return null;
	}

	private BaseText getNameTextRich(T entity)
	{
		BaseText text = getNameText(entity);
		BaseText hoverText = getNameTextHoverText(entity);
		if (hoverText != null)
		{
			TextUtil.attachHoverText(text, hoverText);
		}
		return text;
	}

	// e.g. "[12000] " for gt 12000, note the space at the end
	private BaseText getWorldTimeText(World world)
	{
		return Messenger.s(String.format("[%s] ", world.getTime()), "g");
	}

	private void onLoggingEvent(LoggingType loggingType, Supplier<BaseText[]> supplier)
	{
		LoggerRegistry.getLogger(this.loggerName).log((option) ->
				loggingType.isContainedIn(option) ? supplier.get() : null
		);
	}

	public void onEntityCreated(T entity)
	{
		this.onLoggingEvent(LoggingType.CREATE, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				getNameTextRich(entity),
				String.format("r %s", translator.tr(" created")),
				"g  @ ",
				TextUtil.getCoordinateText("w", entity.getPos(), entity.world.getDimension().getType()),
				"w  ",
				StackTracePrinter.create().ignore(this.getClass()).deobfuscate().toSymbolText()
		)});
	}

	public void onEntityDespawn(T entity)
	{
		this.onLoggingEvent(LoggingType.DESPAWN, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				getNameTextRich(entity),
				String.format("r %s", translator.tr(" despawned")),
				"g  @ ",
				TextUtil.getCoordinateText("w", entity.getPos(), entity.world.getDimension().getType())
		)});
	}

	public void onEntityDied(T entity, DamageSource source, float amount)
	{
		this.onLoggingEvent(LoggingType.DIE, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				TextUtil.getFancyText(
						null,
						TextUtil.getTranslatedName(
								"death.attack." + source.name,
								TextUtil.attachFormatting(getNameTextRich(entity), Formatting.WHITE)
						),
						Messenger.s(String.format("%s: %.1f", translator.tr("damage_amount", "Damage amount"), amount)),
						null
				),
				"g  @ ",
				TextUtil.getCoordinateText("w", entity.getPos(), entity.world.getDimension().getType())
		)});
	}

	public Logger getStandardLogger()
	{
		return ExtensionLoggerRegistry.standardLogger(this.loggerName, LoggingType.DIE.getName(), LoggingType.LOGGING_SUGGESTIONS);
	}

	public enum LoggingType
	{
		DESPAWN("despawn"),
		DIE("die"),
		CREATE("create");

		private final String name;
		public static final	String[] LOGGING_SUGGESTIONS;

		LoggingType(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public boolean isContainedIn(String option)
		{
			return Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}

		static
		{
			List<String> list = Arrays.stream(LoggingType.values()).map(LoggingType::getName).collect(Collectors.toList());
			list.add(Joiner.on(",").join(list));
			LOGGING_SUGGESTIONS = list.toArray(new String[0]);
		}
	}
}
