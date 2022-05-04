package carpettisaddition.logging.loggers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.translations.TranslationContext;
import com.google.common.base.Joiner;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractLogger extends TranslationContext
{
	public final static String MULTI_OPTION_SEP_REG = "[,. ]";
	public final static String OPTION_SEP = ",";

	private final String name;

	public AbstractLogger(String name)
	{
		super("logger." + name);
		this.name = name;
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
		return TISAdditionLoggerRegistry.standardLogger(this.getName(), this.getDefaultLoggingOption(), this.getSuggestedLoggingOption(), true);
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

	public void log(Supplier<BaseText[]> messagePromise)
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

	protected static String createCompoundOption(Iterable<String> options)
	{
		return "\"" + Joiner.on(OPTION_SEP).join(options) + "\"";
	}

	protected static String createCompoundOption(String... options)
	{
		return createCompoundOption(Arrays.asList(options));
	}
}
