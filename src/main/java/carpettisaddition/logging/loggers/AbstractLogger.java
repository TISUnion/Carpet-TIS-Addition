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

package carpettisaddition.logging.loggers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import com.google.common.base.Joiner;
import net.minecraft.network.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

//#if MC >= 11900
//$$ import net.minecraft.network.chat.Component;
//#endif

//#if MC >= 11600
//$$ import com.mojang.brigadier.StringReader;
//#endif

public abstract class AbstractLogger extends TranslationContext
{
	private static final Translator TRANSLATOR = new Translator("logger");
	public final static String MULTI_OPTION_SEP_REG = "[,. ]";
	public final static String OPTION_SEP = ",";

	private final String name;

	@SuppressWarnings("FieldCanBeLocal")
	private final boolean strictOption;

	/**
	 * fabric carpet introduced strictOption thing in mc1.17.1
	 * - false: loggers that accept dynamic/custom options
	 * - true: loggers that don't need an option; loggers that only accept specified options
	 */
	public AbstractLogger(String name, boolean strictOption)
	{
		super(TRANSLATOR.getDerivedTranslator(name));
		this.name = name;
		this.strictOption = strictOption;
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
				wrapOptions(this.getSuggestedLoggingOption())
				//#if MC >= 11700
				//$$ , this.strictOption
				//#endif
		);
	}

	protected void actionWithLogger(Consumer<Logger> action)
	{
		Logger logger = LoggerRegistry.getLogger(this.getName());
		if (logger != null)
		{
			action.accept(logger);
		}
		else
		{
			CarpetTISAdditionServer.LOGGER.warn("Failed to get carpet logger {}", this.getName());
		}
	}

	public void log(
			//#if MC >= 11900
			//$$ Supplier<Component[]> messagePromise
			//#else
			Supplier<BaseComponent[]> messagePromise
			//#endif
	)
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

	// Forcibly log to chat even if it's a hud logger
	public void logToChat(Logger.lMessageIgnorePlayer messagePromise)
	{
		actionWithLogger(logger -> {
			try
			{
				if (logger instanceof HUDLoggerButCanLogToChat)
				{
					((HUDLoggerButCanLogToChat)logger).setShouldLogToChat$TISCM(true);
				}
				logger.log(messagePromise);
			}
			finally
			{
				if (logger instanceof HUDLoggerButCanLogToChat)
				{
					((HUDLoggerButCanLogToChat)logger).setShouldLogToChat$TISCM(false);
				}
			}
		});
	}

	// Utils

	/**
	 * Fabric carpet 1.4.25+ (mc1.16+) uses {@code StringArgumentType.string()} as the option argument in the `/log` command
	 * So we might need to wrap our option with quotes if necessary
	 */
	protected static String wrapOption(@Nullable String option)
	{
		//#if MC >= 11600
		//$$ if (option == null)
		//$$ {
		//$$ 	return null;
		//$$ }
		//$$ boolean requiresQuotes = false;
		//$$ for (int i = 0; i < option.length(); i++)
		//$$ {
		//$$ 	if (!StringReader.isAllowedInUnquotedString(option.charAt(i)))
		//$$ 	{
		//$$ 		requiresQuotes = true;
		//$$ 		break;
		//$$ 	}
		//$$ }
		//$$ if (requiresQuotes)
		//$$ {
		//$$ 	option = "\"" + option.replace("\"", "\"\"") + "\"";
		//$$ }
		//#endif

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
