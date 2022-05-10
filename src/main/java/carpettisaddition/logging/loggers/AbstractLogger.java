package carpettisaddition.logging.loggers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.translations.TranslationContext;
import com.google.common.base.Joiner;
import com.mojang.brigadier.StringReader;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractLogger extends TranslationContext
{
	public final static String MULTI_OPTION_SEP_REG = "[,. ]";
	public final static String OPTION_SEP = ",";

	private final String name;
	private final boolean strictOption;

	public AbstractLogger(String name, boolean strictOption)
	{
		super("logger." + name);
		this.name = name;
		this.strictOption = strictOption;
	}

	public AbstractLogger(String name)
	{
		this(name, true);
	}

	public String getName()
	{
		return this.name;
	}

	// Carpet Logging

	@Nullable
	public String getDefaultLoggingOption()
	{
		String[] suggested = this.getSuggestedLoggingOption();
		return suggested != null && suggested.length > 0 ? suggested[0] : null;
	}

	@Nullable
	public String[] getSuggestedLoggingOption()
	{
		return null;
	}

	public Logger createCarpetLogger()
	{
		return TISAdditionLoggerRegistry.standardLogger(
				this.getName(),
				wrapOption(this.getDefaultLoggingOption()),
				wrapOptions(this.getSuggestedLoggingOption()),
				this.strictOption
		);
	}

	public Logger getLogger()
	{
		return LoggerRegistry.getLogger(this.getName());
	}

	protected void actionWithLogger(Consumer<Logger> action)
	{
		Logger logger = getLogger();
		if (logger != null)
		{
			action.accept(logger);
		}
		else
		{
			CarpetTISAdditionServer.LOGGER.warn("Failed to get carpet logger {}", this.getName());
		}
	}

	public void log(Supplier<Text[]> messagePromise)
	{
		actionWithLogger(logger -> logger.log(messagePromise));
	}

	public void log(Logger.lMessage messagePromise)
	{
		actionWithLogger(logger -> logger.log(messagePromise));
	}

	public void log(Logger.lMessageIgnorePlayer messagePromise)
	{
		actionWithLogger(logger -> logger.log(messagePromise));
	}

	// Utils

	/**
	 * Fabric carpet 1.4.25+ (mc1.16+) uses {@code StringArgumentType.string()} as the option argument in the `/log` command
	 * So we might need to wrap our option with quotes if necessary
	 */
	protected static String wrapOption(@Nullable String option)
	{
		if (option == null)
		{
			return null;
		}
		boolean requiresQuotes = false;
		for (int i = 0; i < option.length(); i++)
		{
			if (!StringReader.isAllowedInUnquotedString(option.charAt(i)))
			{
				requiresQuotes = true;
				break;
			}
		}
		if (requiresQuotes)
		{
			option = "\"" + option.replace("\"", "\"\"") + "\"";
		}
		return option;
	}

	protected static String[] wrapOptions(@Nullable String... options)
	{
		if (options == null)
		{
			return null;
		}
		options = options.clone();
		for (int i = 0; i < options.length; i++)
		{
			options[i] = wrapOption(options[i]);
		}
		return options;
	}

	protected static String createCompoundOption(Iterable<String> options)
	{
		return Joiner.on(OPTION_SEP).join(options);
	}

	protected static String createCompoundOption(String... options)
	{
		return createCompoundOption(Arrays.asList(options));
	}
}
